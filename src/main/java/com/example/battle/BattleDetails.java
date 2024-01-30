package com.example.battle;

import com.example.characters.BaseHero;
import com.example.statistics.AdditionalStatisticsNamesEnum;

public class BattleDetails {

    private int currentInitiative = 0;
    private int initiativePerCycle = 20;

    private final BaseHero hero;
    private final boolean isUserCharacter;


    public BattleDetails(
            BaseHero hero,
            int baseInitiativePerCycle,
            boolean isUserCharacter) {
        this.hero = hero;
        this.isUserCharacter = isUserCharacter;

        this.initiativePerCycle = this.calculateInitiativePerCycle(baseInitiativePerCycle);
    }

    private int calculateInitiativePerCycle(int baseInitiativePerCycle) {
        int enemyInitiative = this.hero.getStats().getAdditionalStatistics().get(AdditionalStatisticsNamesEnum.INITIATIVE).getEffectiveValue();
        float percentAdjust = baseInitiativePerCycle * ((float)enemyInitiative / 100);

        return (baseInitiativePerCycle + (int)percentAdjust);
    }

    public void increaseCurrentInitiative(int value){
        this.currentInitiative += value;
    }
    public void useInitiativePoints(int value){
        if(this.currentInitiative >= value){
            this.currentInitiative -= value;
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

    public void setCurrentInitiative(int currentInitiative) {
        this.currentInitiative = currentInitiative;
    }

    public BaseHero getHero() {
        return hero;
    }

    public boolean isUserCharacter() {
        return isUserCharacter;
    }
}
