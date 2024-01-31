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

    private FightStatus fightStatus = FightStatus.FIGHT;

    private final List<FightTurnReport> fightTurnsReport = new ArrayList<>();

    private final Map<ObjectId, BattleDetails> fightDetails;

    private final List<ObjectId> turnParticipants = new ArrayList<>();

    public enum FightStatus {
        FIGHT, ENEMY_WIN, PLAYER_WIN, DRAW
    }

    public record FightReport (FightStatus status, List<FightTurnReport> report
            /*TODO: add here gained exp, gold etc */){}
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

    private FightStatus checkAndSetFightStatus() {
        long enemiesCount = this.fightDetails.values().stream()
                .filter(details -> !details.isUserCharacter())
                .count();
        if(enemiesCount == 0) this.fightStatus = FightStatus.PLAYER_WIN;


        long userCharactersCount = this.fightDetails.values().stream()
                .filter(BattleDetails::isUserCharacter)
                .count();
        if(userCharactersCount == 0) this.fightStatus = FightStatus.ENEMY_WIN;


        return this.fightStatus;
        //TODO: condition for draw?
    }

    private boolean isFightOngoing() {
        return this.checkAndSetFightStatus().equals(FightStatus.FIGHT);
    }

    private boolean hasFightAvailableLeftTurns(int currentTurn) {
        if(currentTurn< this.maxTurns) return true;

        this.fightStatus = FightStatus.DRAW;
        return false;
    }


    public void startFight(){
        int lowestInitiativePerCycle = fightDetails.values().stream().map(BattleDetails::getInitiativePerCycle).sorted((a, b)->a - b).toList().get(0);
        int cyclesForTurn = (int) Math.ceil((double) this.baseMinimumInitiativeForTurn / lowestInitiativePerCycle);
        int turnCount = 0;
        int cycleCounter = 0;
        //while health enemy and player > 0 && turnCount < 100 - exclude infinite
        while (this.isFightOngoing() && this.hasFightAvailableLeftTurns(turnCount)){
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
                    //Just for now. It ensures that dead enemy will not move - continue
                    if(!this.fightDetails.containsKey(id)) continue;

                    BattleDetails currentHeroDetails = fightDetails.get(id);
                    this.handleHeroTurn(currentHeroDetails, turnReport);

                    if(!this.isFightOngoing()){
                        //true == end of fight
                        this.setLastTurnToEndOfFight();
                        System.out.println("END OF FIGHT");

                        break;
                    }
                };
                turnParticipants.clear();
                fightTurnsReport.add(turnReport);
            }else{
                cycleCounter++;
            }
        }
    }

    private void handleHeroTurn(BattleDetails heroDetails, FightTurnReport currentTurnReport) {
        //TODO: logs to remove
        if(heroDetails.isUserCharacter()) System.out.println("\u001B[34m"+"** User turn "+heroDetails.getHero().getId()+ " **");
        else  System.out.println("\u001B[31m"+"** Enemy turn "+heroDetails.getHero().getId() + " ** ");

        BaseHero heroToAttack = this.getMostThreateningHero(!heroDetails.isUserCharacter());
        CombatReturnData turnData = this.attackAndDefend(heroDetails.getHero(),
                heroToAttack);
        this.checkAndHandleDeath(heroToAttack.getId());
        currentTurnReport.addTurnAction(turnData);
    }

    private void checkAndHandleDeath(ObjectId heroId) {
        if(this.fightDetails.get(heroId).getHero().getHealth() <= 0) {
            System.out.println(this.fightDetails.get(heroId).getHero().getName()+  " is dead :O");
            this.fightDetails.remove(heroId);
        }

    }

    private CombatReturnData attackAndDefend (BaseHero attacker, BaseHero defender){
        AttackReturnData attackData = AttackCalculations.generateAttackValue(attacker, true);
        DefendReturnData defendData = DefendCalculations.defend(defender, attacker, attackData);

        CombatReturnData.AttackDefendData doubledAttackData = attackData.withDoubledAttack() ?
                new CombatReturnData.AttackDefendData(
                        AttackCalculations.generateAttackValue(attacker, false),
                        DefendCalculations.defend(defender, attacker, attackData)
                ) : null;

        return new CombatReturnData(new CombatReturnData.AttackDefendData(attackData, defendData),
                Optional.ofNullable(doubledAttackData));

    }

    public List<FightTurnReport> getFightTurnsReport() {
        return fightTurnsReport;
    }

    public FightStatus getFightStatus() {
        return fightStatus;
    }

    public FightReport getFightReport() {
        return new FightReport(this.fightStatus, this.fightTurnsReport);
    }

    private void setLastTurnToEndOfFight() {
        FightTurnReport lastReport =  this.fightTurnsReport.get(this.fightTurnsReport.size() - 1);
        lastReport.setEndOfFight(true);
    }



}



