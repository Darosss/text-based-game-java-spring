package com.example.enemies;

import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EnemyService {

    public Enemy createRandomEnemy(){
        return new Enemy(
            "Some enemy", RandomUtils.getRandomValueWithinRange(5,40),
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
