package com.textbasedgame.characters.equipment;

import dev.morphia.Datastore;
import com.textbasedgame.characters.equipment.Equipment.UnEquipItemResult;
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

    public UnEquipItemResult unEquipItem(String equipmentId, CharacterEquipmentFieldsEnum slot){
        Optional<CharacterEquipment> equipment = this.findById(equipmentId);
        if(equipment.isPresent()){
            CharacterEquipment equipmentRef = equipment.get();
            UnEquipItemResult unEquippedItem = equipmentRef.unEquipItem(slot);
             this.update(equipment.get());
             return unEquippedItem;
        }
        return new UnEquipItemResult(false, "There is no found character equipment. Please try again latter", Optional.empty());
    }
}
