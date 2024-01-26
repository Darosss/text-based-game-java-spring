package com.example.items;

public enum ItemRarityEnum {
    COMMON("Common", 0),
    UNCOMMON("Uncommon", 10),
    RARE("Rare", 25),
    VERY_RARE("Very Rare", 33),
    EPIC("Epic", 50),
    LEGENDARY("Legendary", 75),
    MYTHIC("Mythic", 100);


    private final String displayName;
    private final int bonusPercentage;

    ItemRarityEnum(String displayName, int bonusPercentage) {

        this.displayName = displayName;
        this.bonusPercentage = bonusPercentage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBonusPercentage() {
        return bonusPercentage;
    }
}
