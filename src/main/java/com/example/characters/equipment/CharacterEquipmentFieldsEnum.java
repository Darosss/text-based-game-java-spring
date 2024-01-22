package com.example.characters.equipment;

public enum CharacterEquipmentFieldsEnum {
    HEAD("head"),
    NECK("neck"),
    LEFT_HAND("left hand"),
    RIGHT_HAND("right hand"),
    CHEST("chest"),
    ARMS("arms"),
    LEFT("legs"),
    FOOTS("foots"),
    L_RING_1("left ring 1"),
    L_RING_2("left ring 2"),
    L_RING_3("left ring 3"),
    L_RING_4("left ring 4"),
    L_RING_5("left ring 5"),
    R_RING_1("right ring 1"),
    R_RING_2("right ring 2"),
    R_RING_3("right ring 3"),
    R_RING_4("right ring 4"),
    R_RING_5("right ring 5");

    private final String displayName;

    CharacterEquipmentFieldsEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
