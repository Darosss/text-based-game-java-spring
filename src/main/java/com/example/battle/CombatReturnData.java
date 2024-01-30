package com.example.battle;

import com.example.battle.data.AttackReturnData;
import com.example.battle.data.DefendReturnData;

public record CombatReturnData(AttackReturnData attackData, DefendReturnData defendData) {

}
