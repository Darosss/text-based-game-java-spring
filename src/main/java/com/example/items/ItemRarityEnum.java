package com.example.items;

public enum ItemRarityEnum {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    VERY_RARE("Very Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary"),
    MYTHIC("Mythic");

    private final String displayName;

    ItemRarityEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
