package com.example.battle.data;

public record AttackBase(int value, AttackStrength strength, boolean isDoubleAttack ) {


    public enum AttackStrength {
        NORMAL, CRITIC, LETHAL
    }
}
