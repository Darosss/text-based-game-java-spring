package com.example.skirmishes;

import dev.morphia.annotations.ExternalEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExternalEntity(target=Dungeons.class)
public class Dungeons {
    private int currentLevel = 1;
    private LocalDateTime canFightDate;
    private final List<DungeonData> completedDungeons = new ArrayList<>();

    public record DungeonData(int level, LocalDateTime finished){}

    public Dungeons(){};

    public int getCurrentLevel() {
        return currentLevel;
    }
    public List<DungeonData> getCompletedDungeons() {
        return completedDungeons;
    }

    public void addCompletedDungeon(DungeonData data) {
        this.completedDungeons.add(data);
    }
    public LocalDateTime getCanFightDate() {
        return canFightDate;
    }
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
    public void increaseCurrentLevel() {
        this.setCurrentLevel(this.currentLevel + 1);
    }
    public void setCanFightDate(LocalDateTime value) {
        this.canFightDate = value;
    }



    public boolean canStartFight(){
        if(this.canFightDate == null) return true;
        return this.canFightDate.isBefore(LocalDateTime.now());
    }
}
