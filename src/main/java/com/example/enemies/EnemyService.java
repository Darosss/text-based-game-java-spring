package com.example.enemies;

import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EnemyService {

    public Enemy createRandomEnemy(int level, Enemy.EnemyType enemyType,
                                   Map<BaseStatisticsNamesEnum, Integer> baseStatistics,
                                   Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics
                                   ){
        return new Enemy("Some enemy", level,enemyType, baseStatistics, additionalStatistics);
    }


    public Enemy createRandomEnemyBasedOnLevel(int level, Enemy.EnemyType type, double statsMultipler){
        int multipledStat = (int) (level * statsMultipler);
        return this.createRandomEnemy(level, type,
                Map.of(
                        BaseStatisticsNamesEnum.STRENGTH, multipledStat,
                        BaseStatisticsNamesEnum.DEXTERITY, multipledStat,
                        BaseStatisticsNamesEnum.CHARISMA, multipledStat,
                        BaseStatisticsNamesEnum.CONSTITUTION, multipledStat,
                        BaseStatisticsNamesEnum.INTELLIGENCE, multipledStat,
                        BaseStatisticsNamesEnum.LUCK, multipledStat
                ),
                Map.of(
                        AdditionalStatisticsNamesEnum.INITIATIVE, multipledStat,
                        AdditionalStatisticsNamesEnum.MAX_HEALTH, multipledStat
                )
        );
    }
}

