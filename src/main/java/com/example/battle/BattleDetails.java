package com.example.battle;

public class BattleDetails {

    private int currentInitiative = 0;
    private int initiativePerCycle = 20;

    public BattleDetails(int initiativePerCycle) {
        this.initiativePerCycle = initiativePerCycle;
    }

    public void increaseCurrentInitiative(int value){
        this.currentInitiative += value;
    }
    public void onExecuteTurn(int max){
        if(this.currentInitiative >= max){
            this.currentInitiative -= max;
        }

    }

    public void addCycleValueToCurrentInitiative(){
        this.currentInitiative += initiativePerCycle;
    }


    public int getCurrentInitiative() {
        return currentInitiative;
    }

    public int getInitiativePerCycle() {
        return initiativePerCycle;
    }

    public void setInitiativePerCycle(int initiativePerCycle) {
        this.initiativePerCycle = initiativePerCycle;
    }


}
