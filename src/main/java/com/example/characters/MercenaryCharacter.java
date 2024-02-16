package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.users.User;
import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target = Character.class, discriminator = "Character")
public class MercenaryCharacter extends Character{
    public MercenaryCharacter() {
    }

    public MercenaryCharacter(String name, User user, CharacterEquipment equipment) {
        super(name, user, equipment);
    }
}
