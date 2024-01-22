package com.example.characters.equipment;

import com.example.items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipmentService {

    private final CharacterEquipmentRepository repository;

    @Autowired
    public EquipmentService(CharacterEquipmentRepository repository) {
        this.repository = repository;
    }
    public CharacterEquipment createForNewCharacter(){
        return repository.save(new CharacterEquipment());
    }

    public CharacterEquipment update(CharacterEquipment equipment) {
        return repository.save(equipment);
    }

    public Optional<CharacterEquipment> findOne(){

        return Optional.ofNullable(repository.findAll().get(0));
    }

    public Optional<CharacterEquipment> findById(String id){
        return repository.findById(id);
    }

    public Item unEquipItem(String equipmentId, CharacterEquipmentFieldsEnum slot){
        Optional<CharacterEquipment> equipment = this.findById(equipmentId);
        if(equipment.isPresent()){
            Item unEquipedItem = equipment.get().unequipItem(slot);
             this.update(equipment.get());

             return unEquipedItem;

        }

        return null;
    }
}
