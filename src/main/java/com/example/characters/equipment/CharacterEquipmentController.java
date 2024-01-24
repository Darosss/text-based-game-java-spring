package com.example.characters.equipment;

import com.example.auth.SecuredRestController;
import com.example.items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class CharacterEquipmentController implements SecuredRestController {
    private EquipmentService service;


    @Autowired
    public CharacterEquipmentController(EquipmentService equipmentService){
        this.service = equipmentService;
    }

    @GetMapping("/equipment")
    public CharacterEquipment getCharacterEquipment() {
        Optional<CharacterEquipment> equipment = this.service.findOne();

        return equipment.orElse(null);


    }
    @GetMapping("/equip/{slot}")
    public ResponseEntity<Item> getCharacterEquipment(@PathVariable CharacterEquipmentFieldsEnum slot) {
        Optional<CharacterEquipment> equipment = this.service.findOne();
        if(equipment.isPresent()){
            return ResponseEntity.status(200).body(equipment.get().getEquippedItemByField(slot));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found eq");

    }

    @PostMapping("/unequip/{equipmentId}/{slot}")
    public Item unEquipBySlot(
            @PathVariable String equipmentId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) {
        try {
            return this.service.unEquipItem(equipmentId, slot);
        }catch(Exception exc){
            throw exc;

        }
    }
}

