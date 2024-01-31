package com.example.battle;

import com.example.battle.data.AttackBase;
import com.example.battle.data.AttackReturnData;
import com.example.battle.data.DefendReturnData;

import java.util.Optional;

public record CombatReturnData(AttackDefendData basicAttack, Optional<AttackDefendData> doubledAttack) {

    public record AttackDefendData(AttackReturnData attack, DefendReturnData defend){};
}
