package com.example.characters;

import com.example.statistics.*;
import dev.morphia.annotations.ExternalEntity;

import java.util.Map;

@ExternalEntity(target = BaseHero.class)
public class BaseHero {
    protected String name;
    protected Integer level = 1;
    protected Integer health = 1;

    protected final Map<BaseStatisticsNamesEnum, BaseStatisticObject> statistics =
            StatisticsUtils.generateDefaultHeroStatistics();
    protected final Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> additionalStatistics =
            StatisticsUtils.generateDefaultHeroAdditionalStatistics();


    protected void updateHealthBasedOnMaxHealth(){
        this.health = this.additionalStatistics.get(AdditionalStatisticsNamesEnum.MAX_HEALTH).getEffectiveValue();
        if(this.health <=0) this.health=1;
    }
    public BaseHero(){};
    public BaseHero(String name) {
        this.name = name;
        this.level = 1;
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

    protected void setHealth(Integer health) {
        this.health = health;
    }
    public Map<BaseStatisticsNamesEnum, BaseStatisticObject> getStatistics() {
        return statistics;
    }

    public Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> getAdditionalStatistics() {
        return additionalStatistics;
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

    public void updateStatistic(BaseStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.statistics.get(statName).updateStatistic(value, updateType);
    }
    public void updateAdditionalStatistic(AdditionalStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.additionalStatistics.get(statName).updateStatistic(value, updateType);
    }

    protected void setAdditionalStatisticsByNameIntegerMap(Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics, StatisticsUtils.StatisticUpdateType updateType){
        additionalStatistics.forEach((k, v)-> this.updateAdditionalStatistic(k, v, updateType));
    }
    protected void setStatisticsByNameIntegerMap(Map<BaseStatisticsNamesEnum, Integer> statistics, StatisticsUtils.StatisticUpdateType updateType){
        statistics.forEach((k, v)-> this.updateStatistic(k, v, updateType));
    }

    public void attack(BaseHero target){

        System.out.println("Im attacking a " + target.getName());
        target.defend(this);
    }

    private void defend(BaseHero attacker){
        System.out.println("Im attacked by" +  attacker.getName());
    }
}
