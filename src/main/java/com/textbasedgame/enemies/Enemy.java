package com.textbasedgame.enemies;


import com.textbasedgame.characters.BaseHero;
import com.textbasedgame.statistics.*;
import org.bson.types.ObjectId;

import java.util.Map;

public class Enemy extends BaseHero {

    private EnemyType type = EnemyType.COMMON;

    public Enemy(){
        super("Default name enemy"); this.setId(new ObjectId());
    }


    //When we want to add an enemy with only base stats
    public Enemy(String name, Integer level,  Map<BaseStatisticsNamesEnum, Integer> baseStatistics) {
        super(name, level, baseStatistics);
        this.setId(new ObjectId());
    }

    //When we want to add an enemy with additional statistics
    public Enemy(String name,Integer level, EnemyType type,
                 Map<BaseStatisticsNamesEnum, Integer> baseStatistics,
                 Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics) {
        super(name, level, baseStatistics, additionalStatistics);
        this.type = type;
        this.setId(new ObjectId());
    }

    public EnemyType getType() {
        return type;
    }
}
