package com.textbasedgame.battle;

import com.textbasedgame.battle.data.AttackBase;
import com.textbasedgame.battle.data.AttackReturnData;
import com.textbasedgame.battle.data.DefendReturnData;

import java.util.Optional;

public record CombatReturnData(AttackDefendData basicAttack, Optional<AttackDefendData> doubledAttack) {

    public record AttackDefendData(AttackReturnData attack, DefendReturnData defend){};
}
