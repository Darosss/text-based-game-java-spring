package com.example.battle.data;

import javax.annotation.Nullable;

//TODO: latter add crit, deadly, two hit? etc
public record AttackReturnData(String name, AttackBase baseValues, @Nullable AttackDebuffs debuffs
) {


}
