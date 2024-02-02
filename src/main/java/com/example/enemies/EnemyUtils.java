package com.example.enemies;

import com.example.skirmishes.EnemySkirmishDifficulty;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.utils.RandomUtils;

import java.util.Map;

public class EnemyUtils {
    public record LevelRange(int min, int max){}
    public static LevelRange getEnemyLevelRanges(EnemySkirmishDifficulty difficulty, int characterLevel){
        return switch (difficulty){
            case NOOB -> new LevelRange(1, characterLevel/2);
            case WEAKER -> new LevelRange(Math.max(1, characterLevel-10), Math.max(1, characterLevel-2) );
            case EQUAL -> new LevelRange(characterLevel, characterLevel);
            case FAIR -> new LevelRange(Math.max(1, characterLevel-1),Math.max(1, characterLevel+1) );
            case STRONGER -> new LevelRange(characterLevel+5, characterLevel+10);
            case IMPOSSIBLE -> new LevelRange(characterLevel+10, characterLevel*2);
        };
    }

    public static double getEnemyStatsMultiplier(EnemyType type){
        return switch (type){
            case COMMON -> 0.5;
            case UNCOMMON -> 1;
            case RARE -> 1.2;
            case EPIC -> 2;
            case BOSS -> 3;
            case ANCESTOR -> 5;
        };
    }

    public static Enemy createRandomEnemy(String name, int level, EnemyType enemyType,
                      Map<BaseStatisticsNamesEnum, Integer> baseStatistics,
                      Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics
    ){
        return new Enemy(name, level,enemyType, baseStatistics, additionalStatistics);
    }

    public static Enemy createRandomEnemyBasedOnLevel(String name, int level, EnemyType type, double statsMultiplier){
        int multipliedStat = (int) (level * statsMultiplier);
        return createRandomEnemy(name, level, type,
                Map.of(
                        BaseStatisticsNamesEnum.STRENGTH, multipliedStat,
                        BaseStatisticsNamesEnum.DEXTERITY, multipliedStat,
                        BaseStatisticsNamesEnum.CHARISMA, multipliedStat,
                        BaseStatisticsNamesEnum.CONSTITUTION, multipliedStat,
                        BaseStatisticsNamesEnum.INTELLIGENCE, multipliedStat,
                        BaseStatisticsNamesEnum.LUCK, multipliedStat
                ),
                Map.of(
                        AdditionalStatisticsNamesEnum.INITIATIVE, multipliedStat,
                        AdditionalStatisticsNamesEnum.MAX_HEALTH, multipliedStat
                )
        );
    }


    public static EnemyType getEnemyTypeDependsOnProbability() {
        return getEnemyTypeDependsOnProbability(1);
    }
    public static EnemyType getEnemyTypeDependsOnProbability(double additionalBonusMultiplier) {
        double randomDouble = RandomUtils.getRandomValueWithinRange(0, 1.0);
        for(EnemyType value: EnemyType.values()){
            double probabilityWithBonus = Math.min(1.0, value.getProbability() * Math.max(0.1, additionalBonusMultiplier));
            if(randomDouble <= probabilityWithBonus) return value;

        }

        return EnemyType.COMMON;
    }

    public static EnemyType getEnemyTypeBasedOnSkirmishDifficulty(EnemySkirmishDifficulty difficulty){
        return getEnemyTypeDependsOnProbability(getMultiplierForEnemyType(difficulty));
    }

    private static double getMultiplierForEnemyType(EnemySkirmishDifficulty difficulty){
        return switch (difficulty){
            case NOOB ->  2.0;
            case WEAKER -> 0.8;
            case EQUAL -> 1.1;
            case FAIR ->  1.5;
            case STRONGER -> 2.2;
            case IMPOSSIBLE -> 3;
        };
    }
}
