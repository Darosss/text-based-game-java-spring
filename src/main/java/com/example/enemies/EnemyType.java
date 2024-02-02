package com.example.enemies;

public enum EnemyType {
    ANCESTOR(0.03),
    BOSS(0.12),
    EPIC(0.25),
    RARE(0.4),
    UNCOMMON(0.7),
    COMMON(1.0);

     private final double probability;

    EnemyType(double probability){
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}


