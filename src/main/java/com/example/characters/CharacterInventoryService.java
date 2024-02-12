package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.characters.equipment.Equipment.UnEquipItemResult;
import com.example.characters.equipment.Equipment.EquipItemResult;

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
    public UnEquipItemResult unEquipItem (ObjectId userId, CharacterEquipmentFieldsEnum slot) throws Exception {
        try(MorphiaSession session = datastore.startSession()) {
            session.startTransaction();
            Inventory userInventory = this.fetchUserInventory(session, userId);
            Character character = this.fetchCharacter(session, userId);
            if (userInventory != null && character != null) {
                UnEquipItemResult transactionDone = this.handleUnEquipTransaction(session, slot, userInventory, character);
            if (transactionDone.success()) {
                session.commitTransaction();
            } else {
                session.abortTransaction();
            }
            return transactionDone;
            }

        return new UnEquipItemResult(false, "Cannot find user inventory and/or character. Contact administration", Optional.empty());

        }catch(Exception e){
            throw new Exception("Something went wrong when trying to equip item");
        }
    }

    private UnEquipItemResult handleUnEquipTransaction(
            MorphiaSession session, CharacterEquipmentFieldsEnum slot,
            Inventory userInventory, Character character
        ){
        CharacterEquipment equipment = character.getEquipment();

        UnEquipItemResult unEquippedItemData = equipment.unEquipItem(slot);

        if (!unEquippedItemData.success() || unEquippedItemData.item().isEmpty()) return unEquippedItemData;

        boolean itemAddedToInv = userInventory.addItem(unEquippedItemData.item().get());

        if(!itemAddedToInv)
            return new UnEquipItemResult(false,
                    "Couldn't remove item and add to inventory. Try again latter" ,
                    Optional.empty()
            );

        this.calculateStatisticsAndSave(session, unEquippedItemData.item().get(), character, userInventory, false);


        return unEquippedItemData;
    }

    public EquipItemResult  equipItem (ObjectId userId, Item item, CharacterEquipmentFieldsEnum slot) throws Exception {
        try(MorphiaSession session = datastore.startSession()) {
            session.startTransaction();
            Inventory userInventory = this.fetchUserInventory(session, userId);
            Character character = this.fetchCharacter(session, userId);


            if (userInventory != null && character != null) {

                EquipItemResult transactionDone = this.handleEquipTransaction(session, item, slot, userInventory, character);
                if(transactionDone.success()) {
                    session.commitTransaction();
                }else {
                    session.abortTransaction();
                }
                return transactionDone;
            }
            return new EquipItemResult(false,
                    "Cannot find user inventory and/or character. Contact administration"
            );

        }catch(Exception e){
            System.out.println(e);
            throw new Exception("Something went wrong when trying to equip item");
        }
    }

    private EquipItemResult  handleEquipTransaction (
            MorphiaSession session, Item item, CharacterEquipmentFieldsEnum slot,
            Inventory userInventory, Character character
    ){
        Optional<Item> itemToEquip = userInventory.removeItemById(item.getId());

        if (itemToEquip.isEmpty()) return new EquipItemResult(false, "Item does not exist.");

        EquipItemResult equippedData = character.getEquipment().equipItem(slot, item);

        if(equippedData.success()) this.calculateStatisticsAndSave(session, itemToEquip.get(), character, userInventory, true);

        return equippedData;
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
