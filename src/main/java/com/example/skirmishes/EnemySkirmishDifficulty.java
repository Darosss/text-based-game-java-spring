package com.example.skirmishes;

public enum EnemySkirmishDifficulty {
    NOOB(10),
    WEAKER(75),
    EQUAL(100),
    FAIR(100),
    STRONGER(75),
    IMPOSSIBLE(10);

    private final double probability;

    EnemySkirmishDifficulty(int probability){
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
