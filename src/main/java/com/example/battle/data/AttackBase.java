package com.example.battle.data;

public record AttackBase(int value, AttackStrengthWithBonusDamage attackStrength) {


    public enum AttackStrength {
        NORMAL, CRITIC, LETHAL
    }

    public record AttackStrengthWithBonusDamage(AttackBase.AttackStrength attackStrength, double percentBonusDamage){}

}
