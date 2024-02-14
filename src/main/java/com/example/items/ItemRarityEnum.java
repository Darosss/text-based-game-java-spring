package com.example.items;

public enum ItemRarityEnum {
    COMMON("Common", 0, 0, 0, 1.0),
    UNCOMMON("Uncommon", 10, 2, 0.5,0.7),
    RARE("Rare", 15, 4, 1.2,0.3),
    VERY_RARE("Very Rare", 15, 8,1.8, 0.16),
    EPIC("Epic", 20, 12, 2.0, 0.09),
    LEGENDARY("Legendary", 20, 16, 2.5, 0.02),
    MYTHIC("Mythic", 30, 20, 4.0, 0.007);


    private final String displayName;
    private final double bonusValue;
    private final double bonusPercentageValue;

    private final double bonusCostValue;
    private final double probability;

    ItemRarityEnum(String displayName, double bonusValue, double bonusPercentageValue, double bonusCostValue, double probability) {
        this.displayName = displayName;
        this.bonusValue = bonusValue;
        this.bonusPercentageValue = bonusPercentageValue;
        this.bonusCostValue = bonusCostValue;
        this.probability = probability;
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

    public double getProbability() {
        return probability;
    }

    public double getBonusCostValue() {
        return bonusCostValue;
    }
}
