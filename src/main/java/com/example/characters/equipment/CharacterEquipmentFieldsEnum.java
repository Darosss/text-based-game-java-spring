package com.example.characters.equipment;

import com.example.items.ItemTypeEnum;

import java.util.EnumSet;

public enum CharacterEquipmentFieldsEnum {
    HEAD("head", EnumSet.of(ItemTypeEnum.HELMET)),
    NECK("neck", EnumSet.of(ItemTypeEnum.NECKLACE)),
    LEFT_HAND("left hand", EnumSet.of(ItemTypeEnum.WEAPON_MELEE, ItemTypeEnum.WEAPON_RANGED, ItemTypeEnum.SHIELD)),
    RIGHT_HAND("right hand", EnumSet.of(ItemTypeEnum.WEAPON_MELEE, ItemTypeEnum.WEAPON_RANGED, ItemTypeEnum.SHIELD)),
    CHEST("chest", EnumSet.of(ItemTypeEnum.CHEST_ARMOR)),
    ARMS("arms", EnumSet.of(ItemTypeEnum.GLOVES)),
    LEGS("legs", EnumSet.of(ItemTypeEnum.LEG_ARMOR)),
    FOOTS("foots", EnumSet.of(ItemTypeEnum.BOOTS)),
    L_RING_1("left ring 1", EnumSet.of(ItemTypeEnum.RING)),
    L_RING_2("left ring 2", EnumSet.of(ItemTypeEnum.RING)),
    R_RING_1("right ring 1", EnumSet.of(ItemTypeEnum.RING)),
    R_RING_2("right ring 2", EnumSet.of(ItemTypeEnum.RING));

    private final String displayName;
    private final EnumSet<ItemTypeEnum> availableItemTypes;

    CharacterEquipmentFieldsEnum(String displayName, EnumSet<ItemTypeEnum> availableItemTypes) {
        this.displayName = displayName;
        this.availableItemTypes = availableItemTypes;

    }

    public EnumSet<ItemTypeEnum> getAvailableItemTypes() {
        return availableItemTypes;
    }

    public String getDisplayName() {
        return displayName;
    }
}
