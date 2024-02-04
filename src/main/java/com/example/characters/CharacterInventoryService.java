package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.items.Item;
import com.example.users.inventory.Inventory;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import dev.morphia.transactions.MorphiaSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CharacterInventoryService {
    private final Datastore datastore;

    @Autowired
    public CharacterInventoryService(Datastore datastore) {
        this.datastore = datastore;
    }
    public boolean unEquipItem (ObjectId userId, CharacterEquipmentFieldsEnum slot) throws Exception {
        try(MorphiaSession session = datastore.startSession()) {
            session.startTransaction();
            Inventory userInventory = this.fetchUserInventory(session, userId);
            Character character = this.fetchCharacter(session, userId);
            boolean transactionDone = false;
            if (userInventory != null && character != null) {
                transactionDone = this.handleUnEquipTransaction(session, slot, userInventory, character);
            }
            if (transactionDone) {
                session.commitTransaction();
                return true;
            } else {
                session.abortTransaction();
                return false;
            }
        }catch(Exception e){
            throw new Exception("Something went wrong when trying to equip item");
        }
    }

    private boolean handleUnEquipTransaction(
            MorphiaSession session, CharacterEquipmentFieldsEnum slot,
            Inventory userInventory, Character character
        ){
        CharacterEquipment equipment = character.getEquipment();
        Optional<Item> unEquippedItem = equipment.unEquipItem(slot);

        if (unEquippedItem.isEmpty()) return false;

        boolean itemAddedToInv = userInventory.addItem(unEquippedItem.get());

        if(!itemAddedToInv) return false;

        this.calculateStatisticsAndSave(session, unEquippedItem.get(), character, userInventory, false);


        return true;
    }

    public boolean equipItem (ObjectId userId, Item item, CharacterEquipmentFieldsEnum slot) throws Exception {
        try(MorphiaSession session = datastore.startSession()) {
            session.startTransaction();
            Inventory userInventory = this.fetchUserInventory(session, userId);
            Character character = this.fetchCharacter(session, userId);
            boolean transactionDone = false;

            if (userInventory != null && character != null) {

                transactionDone = this.handleEquipTransaction(session, item, slot, userInventory, character);
                if(transactionDone) {
                    session.commitTransaction();
                    return true;
                }{
                    session.abortTransaction();
                    return false;
                }

            }

            return transactionDone;
        }catch(Exception e){
            System.out.println(e);
            throw new Exception("Something went wrong when trying to equip item");
        }
    }

    private boolean handleEquipTransaction (
            MorphiaSession session, Item item, CharacterEquipmentFieldsEnum slot,
            Inventory userInventory, Character character
    ){
        Optional<Item> itemToEquip = userInventory.removeItemById(item.getId());

        if (itemToEquip.isEmpty()) return false;

        boolean equipped = character.getEquipment().equipItem(slot, item);
        if(!equipped) return false;

        this.calculateStatisticsAndSave(session, itemToEquip.get(), character, userInventory, true);
        return true;
    }
    private void calculateStatisticsAndSave(MorphiaSession session, Item item, Character character, Inventory userInventory, boolean isEquip) {
        character.calculateStatisticByItem(item, isEquip);
        //TODO: for now save - latter make find.update() ?
        session.save(character);
        session.save(userInventory);
        session.save(character.getEquipment());
    }
    private Inventory fetchUserInventory(MorphiaSession session, ObjectId userId) {
        return session.find(Inventory.class)
                .filter(Filters.eq("user", userId))
                .first();
    }
    private Character fetchCharacter(MorphiaSession session, ObjectId userId) {
        return session.find(Character.class)
                .filter(Filters.eq("user", userId))
                .first();
    }
}
