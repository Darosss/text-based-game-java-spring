package com.example.characters.equipment;
import com.example.characters.equipment.Equipment.EquipItemResult;
import com.example.characters.equipment.Equipment.UnEquipItemResult;
import com.example.characters.Character;
import com.example.items.Item;
import com.example.items.ItemTypeEnum;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Entity("equipments")
    public class CharacterEquipment {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;

    @JsonIgnoreProperties("equipment")
    @Reference(idOnly = true, lazy = true)
    private Character character;

    @Reference(lazy = true)
    private Map<CharacterEquipmentFieldsEnum, Item> slots = new HashMap<>();
    public CharacterEquipment() {}



    public EquipItemResult equipItem(CharacterEquipmentFieldsEnum slot, Item item) {
        if (this.slots.get(slot) != null) return new EquipItemResult(false, "Slot is already equipped");
        boolean correctItemTypeToWear = slot.getAvailableItemTypes().contains(item.getType());
        if(!correctItemTypeToWear) return new EquipItemResult(false, "Item does not have correct type to wear on that slot");

        EquipItemResult result = switch (slot) {
            case LEFT_HAND -> canWearOnHand(item, true);
            case RIGHT_HAND -> canWearOnHand(item, false);

            default -> new EquipItemResult(true, "Successfully equipped item");
        };

        if(result.success()) { this.slots.put(slot, item); }

        return result;

    }
    private EquipItemResult canWearOnHand(Item item, boolean leftHand) {
        Item oppositeHand = this.slots.get(leftHand ? CharacterEquipmentFieldsEnum.RIGHT_HAND : CharacterEquipmentFieldsEnum.LEFT_HAND);
        if (oppositeHand != null) {
            ItemTypeEnum itemType = item.getType();
            ItemTypeEnum slotItemType = oppositeHand.getType();

            String message = "";
            boolean success = true;

             switch(itemType){
                case SHIELD -> {
                    if(slotItemType.equals(itemType)) { success = false; message = "Cannot equip two shields"; }
                    else if(slotItemType.equals(ItemTypeEnum.WEAPON_RANGED)) { success = false; message = "Cannot equip shield with ranged weapon"; }
                }
                case WEAPON_RANGED ->  {
                    if(slotItemType.equals(itemType)) { success = false; message = "Cannot equip two ranged weapons"; }
                    else if(slotItemType.equals(ItemTypeEnum.SHIELD)){ success = false; message = "Cannot equip ranged weapon with shield"; }
                    else if(slotItemType.equals(ItemTypeEnum.WEAPON_MELEE)){ success = false; message = "Cannot equip ranged weapon with melee weapon"; }
                }
                case WEAPON_MELEE -> {
                    if(slotItemType.equals(ItemTypeEnum.WEAPON_RANGED)) { success = false; message = "Cannot equip melee weapon with ranged weapon"; }
                }
            }

            if(!success) return new EquipItemResult(success, message);
        }
        return new EquipItemResult(true, "Successfully equipped item");
    };

    public UnEquipItemResult unEquipItem(CharacterEquipmentFieldsEnum slot){
        if (this.slots.get(slot) != null) {
            Optional<Item> unequippedItem =  Optional.ofNullable(this.slots.remove(slot));
            return new UnEquipItemResult(true, "Successfully unequipped item", unequippedItem);
        }
        return new UnEquipItemResult(true, "There is no item to take off", Optional.empty());

    }
    public Item getEquippedItemByField(CharacterEquipmentFieldsEnum slot) {
        return this.slots.get(slot);
    }

    public ObjectId getId() {
        return id;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public Map<CharacterEquipmentFieldsEnum, Item> getSlots() {
        return slots;
    }

    @Override
    public String toString() {
        return "CharacterEquipment{" +
                "id=" + id +
                ", character=" + character +
                ", slots=" + slots +
                '}';
    }
}
