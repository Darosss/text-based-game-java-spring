package com.example.statistics;

public enum AdditionalStatisticsNamesEnum {
    MIN_DAMAGE("Min damage", 1),
    MAX_DAMAGE("Max damage",2),
    MAX_HEALTH("Max health", 300),
    CRITIC("Critic"),
    LETHAL_CRITIC("Lethal critic"),
    DOUBLE_ATTACK("Double attack"),
    ARMOR("Armor"),
    BLOCK("Block"),
    PARRYING("Parrying"),
    DODGE("Dodge"),
    THREAT("Threat"),
    INITIATIVE("Initiative");



    private final String displayName;
    private final int initialValue;

    public String getDisplayName(){ return displayName; }

    public int getInitialValue() {
        return initialValue;
    }

    AdditionalStatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
        this.initialValue = 0;
    }
    AdditionalStatisticsNamesEnum(String displayName, int initialValue) {
        this.displayName = displayName;
        this.initialValue = initialValue;
    }


}
