package com.example.statistics;

public enum AdditionalStatisticsNamesEnum {
    MIN_DAMAGE("Min damage"),
    MAX_DAMAGE("Max damage"),
    MAX_HEALTH("Max health"),
    CRITIC_CHANCE("Critic chance"),
    LETHAL_CRITIC_CHANCE("Lethal critic chance"),
    DOUBLE_ATTACK_CHANCE("Double attack chance"),
    ARMOR("Armor"),
    INITIATIVE("Initiative");



    private final String displayName;

    public String getDisplayName(){ return displayName; }
    AdditionalStatisticsNamesEnum(String displayName) {
        this.displayName = displayName;
    }


}
