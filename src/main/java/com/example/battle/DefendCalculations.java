package com.example.battle;

import com.example.battle.data.AttackReturnData;
import com.example.battle.data.DefendReturnData;
import com.example.characters.BaseHero;

public class DefendCalculations {
    public static DefendReturnData defend(BaseHero defender, String attacker, AttackReturnData attackData){
        defender.decreaseHealth(attackData.baseValues().value());

        String defendLog = "[DEFEND]"+defender.getName() + " attacked by [" +  attacker + "] for dmg "+ attackData.baseValues().value() +
                " HP left: [" + defender.getHealth()+ "]";
        System.out.println(defendLog);
        return new DefendReturnData(defender.getName(), attackData.baseValues().value(), false);
    }


}
