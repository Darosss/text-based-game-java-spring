package com.example.battle;

import com.example.characters.BaseHero;
import com.example.characters.Character;
import com.example.enemies.Enemy;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class BattleManagerService {
    private final int baseInitiativePerCycle = 20;
    private final int minumumBaseCapInitiative = 50;
    private int baseMinimumInitiativeForTurn = 50;


    private void calculateMinimumInitiativeOfLevelsMean(int[] levels) {
        List<Integer> results = new ArrayList<>();
        for (int levelVal : levels) {
            int result = (int) (levelVal * 1.2 * 1.8);
            results.add(result);
        }
        int value = results.stream().mapToInt(Integer::intValue).sum();

        this.baseMinimumInitiativeForTurn = Math.max(value, minumumBaseCapInitiative);
    }

    private Map<ObjectId, BattleDetails> prepareFightDetails(
            Map<ObjectId, Character> characters, Map<ObjectId, Enemy> enemies
    ) {
        Map<ObjectId, BattleDetails> detailsMap = new HashMap<>();
        characters.values().forEach((charVal) -> {
            detailsMap.compute(charVal.getId(), (v, k) -> {
                int heroInitiative = charVal.getAdditionalStatistics().get(AdditionalStatisticsNamesEnum.INITIATIVE).getEffectiveValue();
                float percentAdjust = baseInitiativePerCycle * ((float)heroInitiative / 100);;
                int initiativeWithPercent =baseInitiativePerCycle + (int)percentAdjust;
                if(k==null) return new BattleDetails(initiativeWithPercent);

                k.setInitiativePerCycle(initiativeWithPercent);
                return k;
            });
        });

        enemies.values().forEach((charVal) -> {
            detailsMap.compute(charVal.getId(), (v, k) -> {
                int enemyInitiative = charVal.getAdditionalStatistics().get(AdditionalStatisticsNamesEnum.INITIATIVE).getEffectiveValue();
                float percentAdjust = baseInitiativePerCycle * ((float)enemyInitiative / 100);
                int initiativeWithPercent = (baseInitiativePerCycle + (int)percentAdjust);
                if(k==null) return new BattleDetails(initiativeWithPercent);

                k.setInitiativePerCycle(initiativeWithPercent);
                return k;
            });
        });

        return detailsMap;
    }

    public List<String> performNormalFight(Map<ObjectId, Character> characters, Map<ObjectId, Enemy> enemies) {
        List<String> battleLogs = new ArrayList<>();
        this.calculateMinimumInitiativeOfLevelsMean(
                IntStream.concat(
                        characters.values().stream().mapToInt(Character::getLevel),
                        enemies.values().stream().mapToInt(Enemy::getLevel)
                ).toArray()
        );

        Map<ObjectId, BattleDetails> fightDetails = this.prepareFightDetails(characters, enemies);
        List<ObjectId> participantsTurn = new ArrayList<>();
        battleLogs.add("baseMinimumInitiativeForTurn for this fight: "+this.baseMinimumInitiativeForTurn);
        //while health enemy and player > 0 && turnCount < 100 - exclude infinite
        for (int turnCount = 0; turnCount < 90; ){
            fightDetails.forEach((k,v)->{
                v.addCycleValueToCurrentInitiative();
                if(v.getCurrentInitiative() > this.baseMinimumInitiativeForTurn) participantsTurn.add(k);
            });

            if(!participantsTurn.isEmpty()){
                System.out.println(turnCount+1 + "\u001B[0m" +"************* TURN STARTED *************");
                turnCount ++;
            }
            participantsTurn.forEach((id)->{
                if(characters.containsKey(id)){
                    System.out.println("\u001B[34m"+"** User turn "+id+ " **");
                    battleLogs.add("** User turn "+id+ " **");

                    battleLogs.add(attackHero(characters.get(id), enemies.values().iterator().next()));
                }else {
                    System.out.println("\u001B[31m"+"** Enemy turn "+id + " ** ");
                    battleLogs.add("** Enemy turn "+id + " ** ");

                    battleLogs.add(attackHero(enemies.get(id), characters.values().iterator().next()));
                }

                fightDetails.get(id).onExecuteTurn(this.baseMinimumInitiativeForTurn);
            });
            participantsTurn.clear();


        }

        return battleLogs;
    }


    public String attackHero(BaseHero attacker, BaseHero defender){
//        System.out.println("* "+attacker.getName() + " -  ATTACKED - " +  defender.getName());
        attacker.attack(defender);
//        return "* "+attacker.getName() + " -  ATTACKED - " +  defender.getName();
        return "";
    }

}
