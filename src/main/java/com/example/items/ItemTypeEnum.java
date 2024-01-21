package com.example.items;

public enum ItemTypeEnum {
    WEAPON_MELEE("Weapon Melee"),
    WEAPON_RANGED("Weapon Ranged"),
    CHEST_ARMOR("Chest Armor"),
    HELMET("Helmet"),
    GLOVES("Gloves"),
    RING("Ring"),
    NECKLACE("Necklace"),
    BOOTS("Boots"),
    LEG_ARMOR("Leg Armor"),
    CONSUMABLE_POTION("Consumable Potion"),
    CONSUMABLE_FOOD("Consumable Food"),
    NEUTRAL("Neutral");

    private final String displayName;

    ItemTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
