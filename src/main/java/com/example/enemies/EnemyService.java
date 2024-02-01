package com.example.enemies;

import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EnemyService {

    public Enemy createRandomEnemy(Enemy.EnemyType enemyType){
        return new Enemy(
            "Some enemy", RandomUtils.getRandomValueWithinRange(5,40),
                enemyType,
                Map.of(
                        BaseStatisticsNamesEnum.STRENGTH, 20,
                        BaseStatisticsNamesEnum.DEXTERITY, 20,
                        BaseStatisticsNamesEnum.CHARISMA, 20,
                        BaseStatisticsNamesEnum.CONSTITUTION, 20,
                        BaseStatisticsNamesEnum.INTELLIGENCE, 20,
                        BaseStatisticsNamesEnum.LUCK, 20
                ),
                Map.of(
                        AdditionalStatisticsNamesEnum.INITIATIVE, 300,
                        AdditionalStatisticsNamesEnum.MAX_HEALTH, 100
                )
        );
    }
}

//public class HelloWorld {
//    public static void calculateExperienceFromEnemy(int playerLevel, int enemyLevel, double typeMod) {
//        int baseExperience = 25;
//        double levelDifference = enemyLevel - playerLevel;
//        double enemyDifFactor = levelDifference >= 0 ?
//                Math.max(1, levelDifference + (4/100 * levelDifference) ) :
//                Math.min(-1, (enemyLevel - levelDifference)  * baseExperience );
//        double scalingFactor =
//                baseExperience * (Math.pow(enemyLevel+(1.2*enemyLevel), 0.3))
//                        +  (baseExperience * enemyDifFactor);
//
//
//        long experience = (long) Math.max(-10000, (baseExperience * scalingFactor * typeMod));
//
//        System.out.println("scaliNg factor: "+scalingFactor+" | experience: " + experience + " | enemy level: " + enemyLevel+ " | player lvl: " + playerLevel + " | diff " + enemyDifFactor +" b + d: " + baseExperience  * enemyDifFactor );
//
//    }
//    public static long calculateExpToLevelUp(int level) {
//        double levelDecimalFactor = (double)level / 30;
//
//        long neededExp = (long) (100 * (Math.pow(level+(1.2*level), (1.4 + levelDecimalFactor))));
//
//        return neededExp;
//    }
//    public static void main(String []args) {
//
//        for(int i = 25; i < 27; i++) {
//            long diff = calculateExpToLevelUp(i) - calculateExpToLevelUp(i-1);
//            System.out.println(i + ": "+ calculateExpToLevelUp(i-1) + " -> " + calculateExpToLevelUp(i)+ " diff: -> " + diff);
//
//            calculateExperienceFromEnemy(i, i-24, 1);
//            calculateExperienceFromEnemy(i, i-20, 1);
//            calculateExperienceFromEnemy(i, i-15, 1);
//            calculateExperienceFromEnemy(i, i-10, 1);
//            calculateExperienceFromEnemy(i, i-6, 1);
//            calculateExperienceFromEnemy(i, i, 1);
//            calculateExperienceFromEnemy(i, i+6, 1);
//            calculateExperienceFromEnemy(i, i+15, 1);
//
//        }
//
//
//
//    }
//
//
//
//
//}