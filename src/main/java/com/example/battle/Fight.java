package com.example.battle;

import com.example.battle.reports.FightReport;
import com.example.battle.reports.FightTurnReport;
import com.example.battle.data.AttackReturnData;
import com.example.characters.BaseHero;
import com.example.characters.Character;
import com.example.battle.data.DefendReturnData;
import com.example.characters.ExperienceUtils;
import com.example.enemies.Enemy;
import com.example.enemies.EnemyUtils;
import com.example.items.Item;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.IntStream;

public class Fight {
    //TODO: later add fight options(bonuses) - for example: loot multiplier from fight, exp multiplier etc. etc;
    private final int maxTurns;
    private final int baseInitiativePerCycle = 20;
    private final int minimumBaseCapInitiative = 50;
    private int baseMinimumInitiativeForTurn = 50;
    private final FightReport fightReport = new FightReport();
    private final Map<ObjectId, BattleDetails<Character>> userHeroesDetails;
    private final Map<ObjectId, BattleDetails<Enemy>> enemyHeroesDetails;

    private final List<ObjectId> turnParticipants = new ArrayList<>();

    Fight(List<Character> characters,List<Enemy> enemies, int maxTurns) {

        this.calculateMinimumInitiativeOfLevelsMean(
                IntStream.concat(
                        characters.stream().mapToInt(Character::getLevel),
                        enemies.stream().mapToInt(Enemy::getLevel)
                ).toArray()
        );
        this.userHeroesDetails = this.prepareUserHeroesDetails(characters);
        this.enemyHeroesDetails = this.prepareEnemiesHeroesDetails(enemies);
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

    private Map<ObjectId, BattleDetails<Character>>  prepareUserHeroesDetails(List<Character> characters){
        Map<ObjectId, BattleDetails<Character>> heroesDetailsMap = new HashMap<>();
        characters.forEach((charVal) -> {
            heroesDetailsMap.put(charVal.getId(),
                    new BattleDetails<>(charVal, baseInitiativePerCycle, true));
        });
        return heroesDetailsMap;
    }

    private Map<ObjectId, BattleDetails<Enemy>>  prepareEnemiesHeroesDetails(List<Enemy> enemies){
        Map<ObjectId, BattleDetails<Enemy>> heroesDetailsMap = new HashMap<>();
        enemies.forEach((charVal) -> {
            heroesDetailsMap.put(charVal.getId(),
                    new BattleDetails<>(charVal, baseInitiativePerCycle, false));
        });
        return heroesDetailsMap;
    }

    //TODO: make it DRY later
    private BaseHero getMostThreateningUserHero () {
        return this.userHeroesDetails.values().stream()
                .map(BattleDetails::getHero)
                .reduce((hero1, hero2) ->
                        hero1.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.THREAT) >
                                hero2.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.THREAT)? hero1 : hero2)
                .get();
    }
    //TODO: make it DRY later
    private BaseHero getMostThreateningEnemyHero () {
        return this.enemyHeroesDetails.values().stream()
                .map(BattleDetails::getHero)
                .reduce((hero1, hero2) ->
                        hero1.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.THREAT) >
                                hero2.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.THREAT)? hero1 : hero2)
                .get();
    }

    private FightReport.FightStatus checkAndSetFightStatus() {
        long enemiesCount = this.enemyHeroesDetails.size();
        if(enemiesCount == 0) this.fightReport.setStatus(FightReport.FightStatus.PLAYER_WIN);


        long userCharactersCount =  this.userHeroesDetails.size();
        if(userCharactersCount == 0) this.fightReport.setStatus(FightReport.FightStatus.ENEMY_WIN);


        return this.fightReport.getStatus();
        //TODO: condition for draw?
    }

    private boolean isFightOngoing() {
        return this.checkAndSetFightStatus().equals(FightReport.FightStatus.FIGHT);
    }

    private boolean hasFightAvailableLeftTurns(int currentTurn) {
        if(currentTurn< this.maxTurns) return true;

        this.fightReport.setStatus(FightReport.FightStatus.DRAW);
        return false;
    }

    private void handleEndOfFight() {
        this.fightReport.setLastTurnToEndOfFight();

        //At end of fight add lastly living heroes
        this.userHeroesDetails.values().forEach((v)->this.fightReport.addToCharacters((Character) v.getHero()));
        this.enemyHeroesDetails.values().forEach((v)->this.fightReport.addToEnemies((Enemy) v.getHero()));
        System.out.println("END OF FIGHT");

    }

    private  <DetailsType extends BaseHero> void handleCycleInitiatives(Map<ObjectId, BattleDetails<DetailsType>> detailsMap) {
        detailsMap.forEach((k, v)->{
            v.addCycleValueToCurrentInitiative();
            while(v.getCurrentInitiative() > this.baseMinimumInitiativeForTurn){
                this.turnParticipants.add(k);
                v.useInitiativePoints(this.baseMinimumInitiativeForTurn);
            }
        });
    }


    private int findLowestInitiativePerCycle(){
        int lowestInitiativeUserCharacters = this.userHeroesDetails.values().stream().map(BattleDetails::getInitiativePerCycle).sorted((a, b)->a - b).toList().get(0);
        int lowestInitiativeEnemiesCharacters = this.enemyHeroesDetails.values().stream().map(BattleDetails::getInitiativePerCycle).sorted((a, b)->a - b).toList().get(0);
        return Math.min(lowestInitiativeUserCharacters, lowestInitiativeEnemiesCharacters);

    }



    public void startFight(){
        int cyclesForTurn = (int) Math.ceil((double) this.baseMinimumInitiativeForTurn / this.findLowestInitiativePerCycle());
        int turnCount = 0;
        int cycleCounter = 0;
        //while health enemy and player > 0 && turnCount < 100 - exclude infinite
        while (this.isFightOngoing() && this.hasFightAvailableLeftTurns(turnCount)){
            this.handleCycleInitiatives(this.userHeroesDetails);
            this.handleCycleInitiatives(this.enemyHeroesDetails);

            if(!this.turnParticipants.isEmpty() && cycleCounter >= cyclesForTurn - 1){
                FightTurnReport turnReport = new FightTurnReport(turnCount);
                //TODO: known bug where hero health =1/0
                // when dies so fast its throwing an error
                System.out.println("\u001B[0m"  +"************* "+ (turnCount+1) + " TURN STARTED *************");
                turnCount ++;
                cycleCounter = 0;
                for(ObjectId id: this.turnParticipants){
                    if(this.userHeroesDetails.containsKey(id)){
                        //TODO: improve here, make DRY
                        BattleDetails<Character> currentHeroDetails = this.userHeroesDetails.get(id);
                        this.handleHeroTurn(currentHeroDetails, turnReport, true);

                    }else if(this.enemyHeroesDetails.containsKey(id)){
                        //TODO: improve here, make DRY
                        BattleDetails<Enemy> currentHeroDetails = this.enemyHeroesDetails.get(id);
                        this.handleHeroTurn(currentHeroDetails, turnReport, false);
                    }

                    if(!this.isFightOngoing()){
                        //true == end of fight
                        this.fightReport.addTurnReport(turnReport);
                        this.handleEndOfFight();
                        break;
                    }
                };
                turnParticipants.clear();
                this.fightReport.addTurnReport(turnReport);
            }else{
                cycleCounter++;
            }
        }
    }



    private <DetailsType extends BaseHero> void handleHeroTurn(BattleDetails<DetailsType> heroDetails, FightTurnReport currentTurnReport, boolean isUserTurn) {
        //TODO: logs to remove
        if(heroDetails.isUserCharacter()) System.out.println("\u001B[34m"+"** User turn "+heroDetails.getHero().getId()+ " **");
        else System.out.println("\u001B[31m"+"** Enemy turn "+heroDetails.getHero().getId() + " ** ");

        BaseHero heroToAttack = isUserTurn ? getMostThreateningEnemyHero() : getMostThreateningUserHero();
        CombatReturnData turnData = this.attackAndDefend(heroDetails.getHero(),
                heroToAttack);
        this.checkAndHandleDeaths(isUserTurn, heroDetails.getHero().getId(), heroToAttack.getId());
        currentTurnReport.addTurnAction(turnData);
    }

    private void checkAndHandleUserCharacterDeath(ObjectId heroId) {
        Character character = (Character) this.userHeroesDetails.get(heroId).getHero();
        if(character.getHealth() <= 0){
            this.fightReport.addToCharacters(character);
            this.userHeroesDetails.remove(heroId);

        }
    }
    private void checkAndHandleEnemyDeath(ObjectId heroId) {
        Enemy enemy = (Enemy) this.enemyHeroesDetails.get(heroId).getHero();
        if(enemy.getHealth() <= 0){
            this.fightReport.increaseGainedExperience(
                    ExperienceUtils.calculateExperienceFromEnemy(this.findUserMainCharacter().getLevel(),enemy.getLevel(),enemy.getType())
            );

            List<Item> loot = EnemyUtils.checkLootFromEnemy(enemy.getType(), enemy.getLevel());
            this.fightReport.addLootItems(loot);

            this.fightReport.addToEnemies(enemy);
            this.enemyHeroesDetails.remove(heroId);

        }
    }

    public void checkAndHandleDeaths(boolean isUserTurn, ObjectId attackerId, ObjectId defenderId) {
        if (isUserTurn) {
            checkAndHandleUserCharacterDeath(attackerId);
            checkAndHandleEnemyDeath(defenderId);
        } else {
            checkAndHandleUserCharacterDeath(defenderId);
            checkAndHandleEnemyDeath(attackerId);
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

    //TODO: maybe this will not be needed - when other heroes will be in separate collection (as items)?
    private Character findUserMainCharacter() {
        return (Character) this.userHeroesDetails.values().stream().filter((v)->{
            Character character = (Character)v.getHero();
            return character.isMainCharacter();
        }).toList().get(0).getHero();


    }
    public FightReport getFightReport() {
        return this.fightReport;
    }
}



