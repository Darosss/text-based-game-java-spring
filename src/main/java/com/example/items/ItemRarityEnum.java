package com.example.items;

public enum ItemRarityEnum {
    COMMON("Common", 0, 0),
    UNCOMMON("Uncommon", 10, 2),
    RARE("Rare", 15, 4),
    VERY_RARE("Very Rare", 15, 8),
    EPIC("Epic", 20, 12),
    LEGENDARY("Legendary", 20, 16),
    MYTHIC("Mythic", 30, 20);


    private final String displayName;
    private final double bonusValue;
    private final double bonusPercentageValue;

    ItemRarityEnum(String displayName, int bonusValue, int bonusPercentageValue) {

        this.displayName = displayName;
        this.bonusValue = bonusValue;
        this.bonusPercentageValue = bonusPercentageValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getBonusValue() {
        return bonusValue;
    }

    public double getBonusPercentageValue() {
        return bonusPercentageValue;
    }
}
