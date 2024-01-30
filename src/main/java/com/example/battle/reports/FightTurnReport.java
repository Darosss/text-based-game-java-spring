package com.example.battle.reports;

import com.example.battle.CombatReturnData;

import java.util.ArrayList;
import java.util.List;

public class FightTurnReport {

    private Integer turnNumber;
    private List<CombatReturnData> actions = new ArrayList<>();


    public FightTurnReport(Integer turnNumber){
        this.turnNumber = turnNumber;

    }

    public void addTurnAction(CombatReturnData data) {
        actions.add(data);

    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public List<CombatReturnData> getActions() {
        return actions;
    }
}
