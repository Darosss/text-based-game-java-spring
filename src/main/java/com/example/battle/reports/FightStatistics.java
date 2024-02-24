package com.example.battle.reports;

public class FightStatistics {

    private int damageDone = 0;
    private int damageReceived = 0;
    private int  derivedAttacks = 0;
    private int receivedAttackCount = 0;

    public int getDamageDone() {
        return damageDone;
    }

    public int getDamageReceived() {
        return damageReceived;
    }

    public int getDerivedAttacks() {
        return derivedAttacks;
    }

    public int getReceivedAttackCount() {
        return receivedAttackCount;
    }
    public void increaseDamageDone (int value) {
        this.damageDone += value;
    }
    public void increaseDamageReceived (int value) {
        this.damageReceived += value;
    }
    public void increaseDerivedAttacks (int value) {
        this.derivedAttacks += value;
    }
    public void increaseReceivedAttackCount (int value) {
        this.receivedAttackCount += value;
    }

}
