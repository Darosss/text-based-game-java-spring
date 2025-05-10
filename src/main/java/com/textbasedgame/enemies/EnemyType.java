package com.textbasedgame.enemies;

public enum EnemyType {
    ANCESTOR(0.03, 0.5, 1.0),
    BOSS(0.12, 1.0, 1.5),
    EPIC(0.25, 1.2, 2.0),
    RARE(0.4, 1.7, 2.5),
    UNCOMMON(0.7, 2.5, 3.0),
    COMMON(1.0, 5.0, 5.0);

     private final double probability;
     private final double bonusExperience;
     private final double bonusGold;

    EnemyType(double probability, double bonusExperience, double bonusGold){
        this.probability = probability;
        this.bonusExperience = bonusExperience;
        this.bonusGold = bonusGold;
    }

    public double getProbability() {
        return probability;
    }

    public double getBonusExperience() {
        return bonusExperience;
    }

    public double getBonusGold() {
        return bonusGold;
    }
}


