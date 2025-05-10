package com.textbasedgame.battle.data;

public record AttackDebuffs(
        boolean causesPoison,
        boolean causesBurn,
        boolean causesStun,
        boolean ignoresArmor,
        boolean piercesShields,
        int armorPen,
        int armorPenPercent
){
}
