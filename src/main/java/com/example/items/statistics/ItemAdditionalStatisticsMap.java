package com.example.items.statistics;

import com.example.statistics.AdditionalStatisticsNamesEnum;

import java.util.HashMap;
import java.util.Map;

public non-sealed class ItemAdditionalStatisticsMap implements ItemStatisticsMap {
    private Map<AdditionalStatisticsNamesEnum, ItemStatisticsObject<AdditionalStatisticsNamesEnum>> statisticsMap = new HashMap<>();

    public ItemAdditionalStatisticsMap() {}
    public ItemAdditionalStatisticsMap(Map<AdditionalStatisticsNamesEnum, ItemStatisticsObject<AdditionalStatisticsNamesEnum>> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public Map<AdditionalStatisticsNamesEnum, ItemStatisticsObject<AdditionalStatisticsNamesEnum>> getStatisticsMap() {
        return statisticsMap;
    }
    public void addStatistic(AdditionalStatisticsNamesEnum name, ItemStatisticsObject<AdditionalStatisticsNamesEnum> statistic){
        this.statisticsMap.put(name, statistic);
    }



    // Additional methods as needed
}