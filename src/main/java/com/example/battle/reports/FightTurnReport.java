package com.example.battle.reports;

import com.example.battle.CombatReturnData;

import java.util.ArrayList;
import java.util.List;

public class FightTurnReport {

    private final Integer turnNumber;
    private final List<CombatReturnData> actions = new ArrayList<>();
    private boolean isEndOfFight;
    public FightTurnReport(Integer turnNumber){
        this.turnNumber = turnNumber;
    }

    public void addTurnAction(CombatReturnData data) {
        this.actions.add(data);

    }

    public Integer getTurnNumber() {
        return this.turnNumber;
    }

    public List<CombatReturnData> getActions() {
        return this.actions;
    }


    public boolean isEndOfFight() {
        return isEndOfFight;
    }

    public void setEndOfFight(boolean endOfFight) {
        this.isEndOfFight = endOfFight;
    }
}
