package com.example.battle;

import com.example.battle.reports.FightTurnReport;
import com.example.battle.data.AttackReturnData;
import com.example.characters.BaseHero;
import com.example.characters.Character;
import com.example.battle.data.DefendReturnData;
import com.example.enemies.Enemy;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Fight {
    private final int maxTurns;
    private final int baseInitiativePerCycle = 20;
    private final int minumumBaseCapInitiative = 50;
    private int baseMinimumInitiativeForTurn = 50;

    private final Map<ObjectId, Character> characters;
    private final Map<ObjectId, Enemy> enemies;
    private final List<FightTurnReport> fightReport = new ArrayList<>();

    private final Map<ObjectId, BattleDetails> fightDetails;

    private final List<ObjectId> turnParticipants = new ArrayList<>();

    Fight(Map<ObjectId, Character> characters, Map<ObjectId, Enemy> enemies, int maxTurns) {
        this.enemies = enemies;
        this.characters = characters;
        this.calculateMinimumInitiativeOfLevelsMean(
                IntStream.concat(
                        characters.values().stream().mapToInt(Character::getLevel),
                        enemies.values().stream().mapToInt(Enemy::getLevel)
                ).toArray()
        );
       this.fightDetails = this.prepareFightDetails(characters, enemies);
       this.maxTurns = maxTurns;

    }

    Fight(Map<ObjectId, Character> characters, Map<ObjectId, Enemy> enemies){
        this(characters, enemies, 50);
    }

    private void calculateMinimumInitiativeOfLevelsMean(int[] levels) {
        List<Integer> results = new ArrayList<>();
        for (int levelVal : levels) {
            int result = (int) (levelVal * 1.2 * 1.8);
            results.add(result);
        }
        int value = results.stream().mapToInt(Integer::intValue).sum();

        this.baseMinimumInitiativeForTurn = Math.max(value, minumumBaseCapInitiative);
    }


    //TODO: make it DRY later
    private Map<ObjectId, BattleDetails> prepareFightDetails(
            Map<ObjectId, Character> characters, Map<ObjectId, Enemy> enemies
    ) {
        Map<ObjectId, BattleDetails> detailsMap = new HashMap<>();
        characters.values().forEach((charVal) -> {
            detailsMap.compute(charVal.getId(), (v, k) -> {
                int heroInitiative = charVal.getStats().getAdditionalStatistics().get(AdditionalStatisticsNamesEnum.INITIATIVE).getEffectiveValue();
                float percentAdjust = baseInitiativePerCycle * ((float)heroInitiative / 100);;
                int initiativeWithPercent =baseInitiativePerCycle + (int)percentAdjust;
                if(k==null) return new BattleDetails(initiativeWithPercent);

                k.setInitiativePerCycle(initiativeWithPercent);
                return k;
            });
        });

        enemies.values().forEach((charVal) -> {
            detailsMap.compute(charVal.getId(), (v, k) -> {
                int enemyInitiative = charVal.getStats().getAdditionalStatistics().get(AdditionalStatisticsNamesEnum.INITIATIVE).getEffectiveValue();
                float percentAdjust = baseInitiativePerCycle * ((float)enemyInitiative / 100);
                int initiativeWithPercent = (baseInitiativePerCycle + (int)percentAdjust);
                if(k==null) return new BattleDetails(initiativeWithPercent);

                k.setInitiativePerCycle(initiativeWithPercent);
                return k;
            });
        });

        return detailsMap;
    }

    private void fightDetailsHerosHelper(Map<ObjectId, BaseHero> participantsMap) {


    }

    public void startFight(){
        int lowestInitiativePerCycle = fightDetails.values().stream().map(BattleDetails::getInitiativePerCycle).sorted((a, b)->a - b).toList().get(0);
        int cyclesForTurn = (int) Math.ceil((double) this.baseMinimumInitiativeForTurn / lowestInitiativePerCycle);
        int turnCount = 0;
        int cycleCounter = 0;
        //while health enemy and player > 0 && turnCount < 100 - exclude infinite
        while (turnCount < 30){
            fightDetails.forEach((k, v)->{
                v.addCycleValueToCurrentInitiative();

                while(v.getCurrentInitiative() > this.baseMinimumInitiativeForTurn){
                    this.turnParticipants.add(k);
                    v.useInitiativePoints(this.baseMinimumInitiativeForTurn);
                }
            });

            if(!this.turnParticipants.isEmpty() && cycleCounter >= cyclesForTurn - 1){
                FightTurnReport turnReport = new FightTurnReport(turnCount);

                System.out.println("\u001B[0m"  +"************* "+ (turnCount+1) + " TURN STARTED *************");
                turnCount ++;
                cycleCounter = 0;
                this.turnParticipants.forEach((id)->{
                    if(characters.containsKey(id)){
                        System.out.println("\u001B[34m"+"** User turn "+id+ " **");
                        Character currentCharacter =  characters.get(id);
                        CombatReturnData turnData = this.attackAndDefend(currentCharacter, enemies.values().iterator().next());
                        turnReport.addTurnAction(turnData);
                    }else {
                        System.out.println("\u001B[31m"+"** Enemy turn "+id + " ** ");
                        Enemy currentEnemy =  enemies.get(id);
                        CombatReturnData turnData = this.attackAndDefend(currentEnemy, characters.values().iterator().next());
                        turnReport.addTurnAction(turnData);

                    }

                });
                turnParticipants.clear();
                fightReport.add(turnReport);
            }else{
                cycleCounter++;
            }
        }



        }

    private CombatReturnData attackAndDefend(BaseHero attacker, BaseHero defender) {
        return this.attackAndDefend(attacker,defender, false);
    }

    private CombatReturnData attackAndDefend (BaseHero attacker, BaseHero defender, boolean asDoubledAttack){
        AttackReturnData attackData = AttackCalculations.generateAttackValue(attacker, asDoubledAttack);
        DefendReturnData defendData = DefendCalculations.defend(defender, attacker.getName(), attackData);

        if(attackData.baseValues().isDoubleAttack()){
            System.out.println("DOUBLED ATTACK TRIGGERED");
            this.attackAndDefend(attacker, defender, true);
        }

        return new CombatReturnData(attackData, defendData);

    }

    public List<FightTurnReport> getFightReport() {
        return fightReport;
    }

}



