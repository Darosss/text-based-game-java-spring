package com.example.items.statistics;

import com.example.statistics.AdditionalStatisticsNamesEnum;
import dev.morphia.annotations.ExternalEntity;

import java.util.HashMap;
import java.util.Map;

@ExternalEntity(target = ItemAdditionalStatisticsMap.class)
public non-sealed class ItemAdditionalStatisticsMap implements ItemStatisticsMap {
    private Map<AdditionalStatisticsNamesEnum, ItemStatisticsObject> statisticsMap = new HashMap<>();

    public ItemAdditionalStatisticsMap() {}
    public ItemAdditionalStatisticsMap(Map<AdditionalStatisticsNamesEnum, ItemStatisticsObject> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public Map<AdditionalStatisticsNamesEnum, ItemStatisticsObject> getStatisticsMap() {
        return statisticsMap;
    }
    public void addStatistic(AdditionalStatisticsNamesEnum name, ItemStatisticsObject statistic){
        this.statisticsMap.put(name, statistic);
    }

    @Override
    public String toString() {
        return "ItemAdditionalStatisticsMap{" +
                "statisticsMap=" + statisticsMap +
                '}';
    }

}