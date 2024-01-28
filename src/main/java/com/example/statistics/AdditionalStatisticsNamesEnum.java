package com.example.statistics;

public enum AdditionalStatisticsNamesEnum {
    MIN_DAMAGE("Min damage"),
    MAX_HEALTH("Max health"),
    MAX_DAMAGE("Max damage"),
    ARMOR("Armor"),
    INITIATIVE("Initiative");



    private final String displayName;

    public String getDisplayName(){ return displayName; }
    AdditionalStatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
    }


}
