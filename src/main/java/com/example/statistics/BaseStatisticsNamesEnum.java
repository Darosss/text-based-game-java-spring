package com.example.statistics;

public enum BaseStatisticsNamesEnum {
    STRENGTH("strength"),
    DEXTERITY("dexterity"),
    INTELLIGENCE("intelligence"),
    CONSTITUTION("constitution"),
    CHARISMA("charisma"),
    LUCK("luck");



    private final String displayName;

    public String getDisplayName(){ return displayName; }
    BaseStatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
    }
}
