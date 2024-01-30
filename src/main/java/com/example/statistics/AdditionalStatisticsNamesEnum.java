package com.example.statistics;

public enum AdditionalStatisticsNamesEnum {
    MIN_DAMAGE("Min damage"),
    MAX_DAMAGE("Max damage"),
    MAX_HEALTH("Max health"),
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

    public String getDisplayName(){ return displayName; }
    AdditionalStatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
    }


}
