package com.example.battle.data;

//TODO: latter add other
public record DefendReturnData(String name, int receivedDamage, int health, DefendType defendType) {

    public enum DefendType {
        NULL, BLOCKED, DODGED, PAIRED
    }
}
