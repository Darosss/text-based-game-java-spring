package com.example.enemies;

public enum EnemyType {
    ANCESTOR(0.03, 0.5),
    BOSS(0.12, 1.0),
    EPIC(0.25, 1.2),
    RARE(0.4, 1.7),
    UNCOMMON(0.7, 2.5),
    COMMON(1.0, 5.0);

     private final double probability;
     private final double bonusExperience;

    EnemyType(double probability, double bonusExperience){
        this.probability = probability;
        this.bonusExperience = bonusExperience;
    }

    public double getProbability() {
        return probability;
    }

    public double getBonusExperience() {
        return bonusExperience;
    }
}


