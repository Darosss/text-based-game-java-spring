package com.example.skirmishes;

import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target = SkirmishData.class)
public class SkirmishData {
    private EnemySkirmishDifficulty difficulty;
    private String name;

    SkirmishData(EnemySkirmishDifficulty difficulty, String name) {
        this.difficulty = difficulty;
        this.name = name;
    }

    public EnemySkirmishDifficulty getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

}
