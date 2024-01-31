package com.example.battle.data;

public record AttackBase(int value, AttackStrengthWithBonusDamage attackStrength, boolean isDoubleAttack ) {


    public enum AttackStrength {
        NORMAL, CRITIC, LETHAL
    }

    public record AttackStrengthWithBonusDamage(AttackBase.AttackStrength attackStrength, double percentBonusDamage){}

}
