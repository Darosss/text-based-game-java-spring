package com.example.battle.reports;

import com.example.battle.CombatReturnData;
import com.example.enemies.Enemy;
import com.example.characters.Character;
import com.example.items.Item;
import org.bson.types.ObjectId;

import java.util.*;

public class FightReport {
    public enum FightStatus {
        FIGHT, ENEMY_WIN, PLAYER_WIN
    }
    private FightStatus status = FightStatus.FIGHT;
    private long gainedExperience = 0L;
    private final List<Item> loot = new ArrayList<>();
    private final List<FightTurnReport> turnsReports = new ArrayList<>();

    private final List<Character> characters = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final HashMap<ObjectId, FightStatistics> statistics = new HashMap<>();
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

    public List<Character> getCharacters() {
        return characters;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void addToCharacters(Character character) {
        this.characters.add(character);
    }
    public void addToEnemies(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public List<Item> getLoot() {
        return loot;
    }

    public void addLootItem(Item item){
        this.loot.add(item);
    }
    public void addLootItems(List<Item> item){
        this.loot.addAll(item);
    }

    public HashMap<ObjectId, FightStatistics> getStatistics() {
        return statistics;
    }
    public void addInitialParticipantsStatistics(List<ObjectId> ids) {
        for (ObjectId id : ids) {
            this.statistics.put(id, new FightStatistics());
        }
    }

    public void increaseStatisticsBasedOnTurn(ObjectId attackerId, ObjectId defenderId, CombatReturnData data) {
        FightStatistics attackerStats = this.statistics.get(attackerId);
        FightStatistics defenderStats = this.statistics.get(defenderId);


        int attackDmg  = data.basicAttack().defend().receivedDamage();
        int doubledAttackDmg = data.doubledAttack().map((val)->val.defend().receivedDamage()).orElse(0);
        int attacksCount = data.doubledAttack().isPresent() ? 2 : 1;
        attackerStats.increaseDamageDone(attackDmg + doubledAttackDmg);
        attackerStats.increaseDerivedAttacks(attacksCount);

        defenderStats.increaseDamageReceived(attackDmg + doubledAttackDmg);
        defenderStats.increaseReceivedAttackCount(attacksCount);

        Optional<CombatReturnData> parryAttack = data.basicAttack().defend().parryAttack();

        if(parryAttack.isPresent()) {
            CombatReturnData parryInstance = parryAttack.get();

            attackerStats.increaseDamageReceived(parryInstance.basicAttack().defend().receivedDamage());
            attackerStats.increaseReceivedAttackCount(1);

            defenderStats.increaseDamageDone(parryInstance.basicAttack().defend().receivedDamage());
            defenderStats.increaseDerivedAttacks(1);
        }

    }

    public Optional<ObjectId> findParticipantIdByHighestDamageDone() {
        return statistics.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue(Comparator.comparingInt(FightStatistics::getDamageDone)))
                .map(Map.Entry::getKey);
    }
}
