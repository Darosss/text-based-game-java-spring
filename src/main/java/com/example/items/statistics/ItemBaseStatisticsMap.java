package com.example.items.statistics;

import com.example.statistics.BaseStatisticsNamesEnum;

import java.util.HashMap;
import java.util.Map;

public non-sealed class ItemBaseStatisticsMap implements ItemStatisticsMap {
    private Map<BaseStatisticsNamesEnum, ItemStatisticsObject<BaseStatisticsNamesEnum>> statisticsMap = new HashMap<>();

    public ItemBaseStatisticsMap() {}
    public ItemBaseStatisticsMap(Map<BaseStatisticsNamesEnum, ItemStatisticsObject<BaseStatisticsNamesEnum>> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public void setStatisticsMap(Map<BaseStatisticsNamesEnum, ItemStatisticsObject<BaseStatisticsNamesEnum>> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public void addStatistic(BaseStatisticsNamesEnum name, ItemStatisticsObject<BaseStatisticsNamesEnum> statistic){
        this.statisticsMap.put(name, statistic);
    }

    public Map<BaseStatisticsNamesEnum, ItemStatisticsObject<BaseStatisticsNamesEnum>> getStatisticsMap() {
        return statisticsMap;
    }
    // Additional methods as needed
}

