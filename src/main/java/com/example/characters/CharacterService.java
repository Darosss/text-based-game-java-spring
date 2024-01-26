package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.characters.equipment.EquipmentService;
import com.example.items.Item;
import com.example.users.User;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {
    private Datastore datastore;
    private EquipmentService equipmentService;

    @Autowired
    public CharacterService(Datastore datastore, EquipmentService equipmentService) {
        this.datastore = datastore;
        this.equipmentService = equipmentService;
    }

    //TODO: make update better later, for now w/e
    public Character update(Character character) {
        return datastore.save(character);
    }

    public List<Character> findAll() {
        return datastore.find(Character.class).stream().toList();
    }

    public List<Character> findUserCharacters(String userId) {
        return datastore.find(Character.class).filter(Filters.eq("user", new ObjectId(userId))).stream().toList();
    }

    public Character create(User user) {
         CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        Character character = new Character(user, equipment);
        Character savedCharacter = datastore.save(character);
        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;

    }
    public Character createDebugCharacter(
            User user,
            int health, long exp, int attack,
            int defense,int agility, int maxHealth
    ) {
        CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        Character character = new Character(user, equipment, health, exp, attack, defense, agility, maxHealth);
        Character savedCharacter = datastore.save(character);

        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;
    }


    public boolean equipItem(String characterId, CharacterEquipmentFieldsEnum slot, Item item) {
        Optional<Character> character = this.findById(characterId);
        if(character.isPresent()){
            Character characterInst = character.get();
            CharacterEquipment equipment = characterInst.getEquipment();
            boolean equipped = equipment.equipItem(slot, item);
            if(equipped) {
                this.equipmentService.update(equipment);

                characterInst.calculateStatisticByItem(item, true);
                this.update(characterInst);
                return true;
            }
        }
        return false;

    }

    public Item unequipItem(String characterId, CharacterEquipmentFieldsEnum slot) {
        Optional<Character> character = this.findById(characterId);
        if(character.isPresent()){
            Character characterInst = character.get();
            CharacterEquipment equipment = characterInst.getEquipment();
            Item unequipedItem = equipment.unequipItem(slot);
            this.equipmentService.update(equipment);
            if(unequipedItem != null) {
                characterInst.calculateStatisticByItem(unequipedItem, false);

                this.update(characterInst);
                return unequipedItem;
            }

        }
        return null;
    }


    public Optional<Character> findById(String id) {
        return Optional.ofNullable(datastore.find(Character.class).filter(Filters.eq("id", new ObjectId(id))).first());
    }
    public Optional<Character> findOneByUserId(String userId) {
        return Optional.ofNullable(datastore.find(Character.class).filter(Filters.eq("user", new ObjectId(userId))).first());
    }

    public Character findOne(){
        return datastore.find(Character.class).first();
    }

}
