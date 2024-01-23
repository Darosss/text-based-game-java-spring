package com.example.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AdditionalStatisticsNamesEnum {
    MIN_DAMAGE("Min damage"),
    MAX_DAMAGE("Max damage"),
    ARMOR("Armor");



    private final String displayName;

    public String getDisplayName(){ return displayName; }
    AdditionalStatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
    }


}
