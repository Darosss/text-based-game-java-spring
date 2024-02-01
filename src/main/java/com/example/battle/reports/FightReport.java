package com.example.battle.reports;

import java.util.ArrayList;
import java.util.List;

public class FightReport {
    public enum FightStatus {
        FIGHT, ENEMY_WIN, PLAYER_WIN, DRAW
    }
    private FightStatus status = FightStatus.FIGHT;
    private long gainedExperience = 0L;
    private final List<FightTurnReport> turnsReports = new ArrayList<>();
    public FightReport(){}

    public List<FightTurnReport> getTurnsReports() {
        return turnsReports;
    }

    public FightStatus getStatus() {
        return status;
    }

    public void setStatus(FightStatus status) {
        this.status = status;
    }

    public long getGainedExperience() {
        return gainedExperience;
    }

    public void setGainedExperience(long gainedExperience) {
        this.gainedExperience = gainedExperience;
    }

    public void increaseGainedExperience(long gainedExperience) {
        this.gainedExperience += gainedExperience;
    }

    public void addTurnReport(FightTurnReport report) {
        this.turnsReports.add(report);
    }

    public void setLastTurnToEndOfFight() {
        FightTurnReport lastReport =  this.turnsReports.get(this.turnsReports.size() - 1);
        lastReport.setEndOfFight(true);
    }
}
