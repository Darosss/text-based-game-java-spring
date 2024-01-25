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

@RestController("equipment")
public class CharacterEquipmentController implements SecuredRestController {
    private EquipmentService service;

    @Autowired
    public CharacterEquipmentController(EquipmentService equipmentService){
        this.service = equipmentService;
    }

    @GetMapping("/get-character-equiped-item/debug/{equipmentId}/{slot}")
    public ResponseEntity<Item> getCharacterEquipmentDebug(
            @PathVariable String equipmentId,
            @PathVariable CharacterEquipmentFieldsEnum slot) {
        Optional<CharacterEquipment> equipment = this.service.findById(equipmentId);
        if(equipment.isPresent()){
            return ResponseEntity.status(200).body(equipment.get().getEquippedItemByField(slot));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found eq");

    }
    @PostMapping("/unequip/debug/{equipmentId}/{slot}")
    public Item unEquipBySlotDebug(
            @PathVariable String equipmentId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) {
        return this.service.unEquipItem(equipmentId, slot);
    }
    @GetMapping("/debug/get-random-equipment")
    public CharacterEquipment getCharacterEquipment() {
        Optional<CharacterEquipment> equipment = this.service.findOne();

        return equipment.orElse(null);
    }

}

