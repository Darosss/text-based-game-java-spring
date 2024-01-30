package com.example.enemies;


import com.example.characters.BaseHero;
import com.example.statistics.*;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

public class Enemy extends BaseHero {

    public Enemy(){
        super("Default name enemy"); this.setId(new ObjectId());
    }
    //When we want to add an enemy with only base stats
    public Enemy(String name, Integer level,  Map<BaseStatisticsNamesEnum, Integer> baseStatistics) {
        super(name, level, baseStatistics);
        this.setId(new ObjectId());
    }

    //When we want to add an enemy with additional statistics
    public Enemy(String name,Integer level,
                 Map<BaseStatisticsNamesEnum, Integer> baseStatistics,
                 Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics) {
        super(name, level, baseStatistics, additionalStatistics);
        this.setId(new ObjectId());
    }


}
