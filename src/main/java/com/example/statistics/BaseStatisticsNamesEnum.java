package com.example.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
public enum BaseStatisticsNamesEnum {
       STRENGTH("Strength"),
       DEXTERITY("Dexterity"),
       INTELLIGENCE("Intelligence"),
       CONSTITUTION("Constitution"),
       CHARISMA("Charisma"),
       LUCK("Luck");



    private final String displayName;
    public String getDisplayName(){ return displayName; }
    BaseStatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
    }

}
