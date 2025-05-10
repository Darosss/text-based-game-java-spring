package com.textbasedgame.battle.data;

import com.textbasedgame.battle.CombatReturnData;

import java.util.Optional;

//TODO: latter add other
public record DefendReturnData(String name, int receivedDamage, int health,
                               DefendType defendType, Optional<CombatReturnData> parryAttack) {

    public enum DefendType {
        NULL, BLOCKED, DODGED, PAIRED
    }
}
