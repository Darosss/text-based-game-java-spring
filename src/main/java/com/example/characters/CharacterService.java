package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.characters.equipment.EquipmentService;
import com.example.items.Item;
import com.example.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {
    private CharacterRepository repository;
    private EquipmentService equipmentService;

    @Autowired
    public CharacterService(CharacterRepository repository, EquipmentService equipmentService) {
        this.repository = repository;
        this.equipmentService = equipmentService;
    }

    public List<Character> findAll() {
        return repository.findAll();
    }

    public Character create(User user) {
         CharacterEquipment equipment = this.equipmentService.createForNewCharacter();

        Character character = new Character(user, equipment);
        Character savedCharacter = this.repository.save(character);
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

        Character character = new Character(user, equipment,health, exp, attack, defense, agility, maxHealth);
        Character savedCharacter = this.repository.save(character);
        equipment.setCharacter(savedCharacter);
        this.equipmentService.update(equipment);

        return savedCharacter;
    }

    public boolean equipItem(String characterId, CharacterEquipmentFieldsEnum slot, Item item) {
        Optional<Character> character = this.findById(characterId);
        if(character.isPresent()){
            CharacterEquipment equipment = character.get().getEquipment();
            boolean equipped = equipment.equipItem(slot, item);
            if(equipped) {
                this.equipmentService.update(equipment);

                return true;
            }
        }
        return false;

    }

    public Item unequipItem(String characterId, CharacterEquipmentFieldsEnum slot) {
        Optional<Character> character = this.findById(characterId);
        if(character.isPresent()){
            CharacterEquipment equipment = character.get().getEquipment();
            return equipment.unequipItem(slot);
        }
        return null;
    }


    public Optional<Character> findById(String id) {
        return repository.findById(id);
    }

    public Optional<Character> findOne(){

        return Optional.ofNullable(repository.findAll().get(0));
    }

}
