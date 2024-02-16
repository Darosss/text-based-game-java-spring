package com.example.characters;

import com.example.statistics.*;
import org.bson.types.ObjectId;

import java.util.Map;

public class BaseHero {
    private ObjectId baseHeroId;
    protected String name;
    protected Integer level = 1;
    protected Integer health = 1;

    protected final HeroStatisticsObject stats = new HeroStatisticsObject();
    protected void updateHealthBasedOnMaxHealth(){
        this.health = this.stats.getMaxHealthEffValue();
    }
    public BaseHero(){};
    public BaseHero(String name) {
        this.name = name;
        this.level = 1;
        //TODO: for now every hero has basic max 300 hp
        this.stats.updateAdditionalStatistic(AdditionalStatisticsNamesEnum.MAX_HEALTH, 300, StatisticsUtils.StatisticUpdateType.VALUE);
        this.updateHealthBasedOnMaxHealth();
    }
    public BaseHero(String name, int level) {
        this(name);
        this.level = level;
    }
    public BaseHero(String name, int level,
                    Map<BaseStatisticsNamesEnum, Integer> baseStatistics) {
        this(name, level);
        this.setStatisticsByNameIntegerMap(baseStatistics, StatisticsUtils.StatisticUpdateType.VALUE);

    }
    public BaseHero(String name, int level,
                    Map<BaseStatisticsNamesEnum, Integer> baseStatistics,
                    Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics
    ) {
        this(name, level, baseStatistics);
        this.setAdditionalStatisticsByNameIntegerMap(additionalStatistics, StatisticsUtils.StatisticUpdateType.VALUE);
    }

    public Integer getLevel() {
        return level;
    }
    public Integer getHealth() {
        return health;
    }

    protected void setHealth(int newHealth) {
        if(newHealth > 0) this.health = Math.min(newHealth, this.stats.getMaxHealthEffValue());
        else this.health = 0;
    }

    public void decreaseHealth(int value) {
        this.setHealth(this.health - value);
    }

    public void increaseHealth(int value){
        this.setHealth(this.health + value);
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void setAdditionalStatisticsByNameIntegerMap(Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics, StatisticsUtils.StatisticUpdateType updateType){
        additionalStatistics.forEach((k, v)-> this.stats.updateAdditionalStatistic(k, v, updateType));
    }
    protected void setStatisticsByNameIntegerMap(Map<BaseStatisticsNamesEnum, Integer> statistics, StatisticsUtils.StatisticUpdateType updateType){
        statistics.forEach((k, v)-> this.stats.updateStatistic(k, v, updateType));
    }

    public int getAdditionalStatEffective(AdditionalStatisticsNamesEnum name) {
        return this.stats.getAdditionalStatistics().get(name).getEffectiveValue();
    }

    public HeroStatisticsObject getStats() {
        return stats;
    }

    public ObjectId getId() {
        return baseHeroId;
    }
    protected void setId(ObjectId id){
        this.baseHeroId = id;
    }
}
