package com.example.characters.equipment;

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

import java.util.EnumSet;
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

    public boolean equipItem(CharacterEquipmentFieldsEnum slot, Item item) {
        if (this.slots.get(slot) != null) return false;
        boolean canWear = switch (slot) {
            case HEAD -> canWearOnHead(item);
            case LEFT_HAND -> canWearOnLHand(item);
            case RIGHT_HAND -> canWearOnRHand(item);
            default -> false;
        };

        if(canWear) { this.slots.put(slot, item); }
        return canWear;
    }


    private boolean isCorrectItemToWear(EnumSet<ItemTypeEnum> requiredTypes, ItemTypeEnum itemType) {
        return requiredTypes.contains(itemType);
    }
    private boolean canWearOnHead(Item item) {
        EnumSet<ItemTypeEnum> requiredTypes = EnumSet.of(ItemTypeEnum.HELMET);
        return  isCorrectItemToWear(requiredTypes, item.getType());
    }
    private boolean canWearOnLHand(Item item) {
        EnumSet<ItemTypeEnum> requiredTypes = EnumSet.of(ItemTypeEnum.WEAPON_MELEE, ItemTypeEnum.WEAPON_RANGED);
        return isCorrectItemToWear(requiredTypes, item.getType());
    }
    private boolean canWearOnRHand(Item item) {
        EnumSet<ItemTypeEnum> requiredTypes = EnumSet.of(ItemTypeEnum.WEAPON_MELEE, ItemTypeEnum.WEAPON_RANGED);
        return isCorrectItemToWear(requiredTypes, item.getType());
    }
    public Optional<Item> unEquipItem(CharacterEquipmentFieldsEnum slot){
        if (this.slots.get(slot) != null) {
            return Optional.ofNullable(this.slots.remove(slot));
        }
        return Optional.empty();
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
