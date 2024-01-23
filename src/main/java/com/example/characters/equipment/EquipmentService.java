package com.example.characters.equipment;

import com.example.characters.Character;
import com.example.items.Item;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipmentService {

    private final Datastore datastore;


    @Autowired
    public EquipmentService(Datastore datastore) {
        this.datastore = datastore;
    }

    public CharacterEquipment createForNewCharacter(){
        return datastore.save(new CharacterEquipment());
    }

    //TODO: make update better later, for now w/e
    public CharacterEquipment update(CharacterEquipment equipment) {
        return datastore.save(equipment);
    }

    public Optional<CharacterEquipment> findOne(){
        return Optional.ofNullable((datastore.find(CharacterEquipment.class).first()));
    }

    public Optional<CharacterEquipment> findById(String id){
        return Optional.ofNullable(datastore.find(CharacterEquipment.class).filter(Filters.eq("id", new ObjectId(id))).first());
    }

    public Item unEquipItem(String equipmentId, CharacterEquipmentFieldsEnum slot){
        Optional<CharacterEquipment> equipment = this.findById(equipmentId);
        if(equipment.isPresent()){
            CharacterEquipment equipmentRef = equipment.get();
            Item unEquipedItem = equipmentRef.unequipItem(slot);
             this.update(equipment.get());
             return unEquipedItem;

        }
        return null;
    }
}
