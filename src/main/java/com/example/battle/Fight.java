package com.example.battle;

import com.example.battle.reports.FightTurnReport;
import com.example.battle.data.AttackReturnData;
import com.example.characters.BaseHero;
import com.example.characters.Character;
import com.example.battle.data.DefendReturnData;
import com.example.enemies.Enemy;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.IntStream;

public class Fight {
    private final int maxTurns;
    private final int baseInitiativePerCycle = 20;
    private final int minimumBaseCapInitiative = 50;
    private int baseMinimumInitiativeForTurn = 50;

    private final List<FightTurnReport> fightReport = new ArrayList<>();

    private final Map<ObjectId, BattleDetails> fightDetails;

    private final List<ObjectId> turnParticipants = new ArrayList<>();

    Fight(List<Character> characters,List<Enemy> enemies, int maxTurns) {

        this.calculateMinimumInitiativeOfLevelsMean(
                IntStream.concat(
                        characters.stream().mapToInt(Character::getLevel),
                        enemies.stream().mapToInt(Enemy::getLevel)
                ).toArray()
        );
       this.fightDetails = this.prepareFightDetails(characters, enemies);
       this.maxTurns = maxTurns;

    }

    Fight(List<Character> characters, List<Enemy> enemies){
        this(characters, enemies, 50);
    }

    private void calculateMinimumInitiativeOfLevelsMean(int[] levels) {
        List<Integer> results = new ArrayList<>();
        for (int levelVal : levels) {
            int result = (int) (levelVal * 1.2 * 1.8);
            results.add(result);
        }
        int value = results.stream().mapToInt(Integer::intValue).sum();

        this.baseMinimumInitiativeForTurn = Math.max(value, minimumBaseCapInitiative);
    }


    //TODO: make it DRY later
    private Map<ObjectId, BattleDetails> prepareFightDetails(
            List<Character> characters, List<Enemy> enemies
    ) {
        Map<ObjectId, BattleDetails> detailsMap = new HashMap<>();
        characters.forEach((charVal) -> {
            detailsMap.put(charVal.getId(),
                    new BattleDetails(charVal, baseInitiativePerCycle, true));
        });

        enemies.forEach((charVal) -> {
            detailsMap.put(charVal.getId(),
                    new BattleDetails(charVal, baseInitiativePerCycle, false));
        });

        return detailsMap;
    }

    private BaseHero getMostThreateningHero (boolean userCharacter) {
        return fightDetails.values().stream()
                .filter(v -> v.isUserCharacter() == userCharacter)
                .map(BattleDetails::getHero)
                .reduce((hero1, hero2) ->
                        hero1.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.THREAT) >
                        hero2.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.THREAT)? hero1 : hero2)
                .get();
    }

    private boolean enoughParticipantsToFight() {
        long userCharactersCount = this.fightDetails.values().stream()
                .filter(BattleDetails::isUserCharacter)
                .count();

        long enemiesCount = this.fightDetails.values().stream()
                .filter(details -> !details.isUserCharacter())
                .count();

        //TODO: Note remove those logs - for debug leave right now

        boolean condition = userCharactersCount > 0 && enemiesCount > 0;

        if(!condition) {
            System.out.println("Fight ended because no enemies or players alive");
        }
        return condition;
    }


    public void startFight(){
        int lowestInitiativePerCycle = fightDetails.values().stream().map(BattleDetails::getInitiativePerCycle).sorted((a, b)->a - b).toList().get(0);
        int cyclesForTurn = (int) Math.ceil((double) this.baseMinimumInitiativeForTurn / lowestInitiativePerCycle);
        int turnCount = 0;
        int cycleCounter = 0;
        //while health enemy and player > 0 && turnCount < 100 - exclude infinite
        while (turnCount < this.maxTurns){
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
                for(ObjectId id: this.turnParticipants){
                    if(!this.enoughParticipantsToFight()){
                        System.out.println("END OF FIGHT");
                        fightReport.add(turnReport);
                        this.setLastTurnToEndOfFight();
                        return;
                    }
                    //Just for now. It ensures that dead enemy will not move - continue
                    if(!this.fightDetails.containsKey(id)) continue;


                    BattleDetails currentHeroDetails = fightDetails.get(id);


                    if(currentHeroDetails.isUserCharacter()){
                        System.out.println("\u001B[34m"+"** User turn "+id+ " **");
                        BaseHero enemyToAttack = this.getMostThreateningHero(false);
                        CombatReturnData turnData = this.attackAndDefend(currentHeroDetails.getHero(),
                                enemyToAttack);
                        this.checkAndHandleDeath(enemyToAttack.getId());
                        turnReport.addTurnAction(turnData);
                    }else {
                        System.out.println("\u001B[31m"+"** Enemy turn "+id + " ** ");
                        BaseHero userToAttack = this.getMostThreateningHero(true);
                        CombatReturnData turnData = this.attackAndDefend(currentHeroDetails.getHero(),
                                userToAttack);
                        this.checkAndHandleDeath(userToAttack.getId());
                        turnReport.addTurnAction(turnData);

                    }

                };
                turnParticipants.clear();
                fightReport.add(turnReport);
            }else{
                cycleCounter++;
            }
        }
    }

    private void checkAndHandleDeath(ObjectId heroId) {
        if(this.fightDetails.get(heroId).getHero().getHealth() <= 0) {
            System.out.println(heroId + " is dead :O");
            this.fightDetails.remove(heroId);
        }

    }

    private CombatReturnData attackAndDefend(BaseHero attacker, BaseHero defender) {
        return this.attackAndDefend(attacker,defender, false);
    }

    private CombatReturnData attackAndDefend (BaseHero attacker, BaseHero defender, boolean asDoubledAttack){
        AttackReturnData attackData = AttackCalculations.generateAttackValue(attacker, asDoubledAttack);
        DefendReturnData defendData = DefendCalculations.defend(defender, attacker, attackData);

        if(attackData.baseValues().isDoubleAttack()){
            System.out.println("DOUBLED ATTACK TRIGGERED");
            this.attackAndDefend(attacker, defender, true);
        }

        return new CombatReturnData(attackData, defendData);

    }

    public List<FightTurnReport> getFightReport() {
        return fightReport;
    }

    private void setLastTurnToEndOfFight() {
        FightTurnReport lastReport =  this.fightReport.get(this.fightReport.size() - 1);
        lastReport.setEndOfFight(true);
    }



}



