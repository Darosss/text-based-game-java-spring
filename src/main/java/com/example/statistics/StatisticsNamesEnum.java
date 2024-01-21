package com.example.statistics;

public enum StatisticsNamesEnum {
    MIN_DAMAGE("Min damage"),
    MAX_DAMAGE("Max damage"),
    ARMOR("Armor");



    private final String displayName;

    public String getDisplayName(){ return displayName; }
    StatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
    }
}
