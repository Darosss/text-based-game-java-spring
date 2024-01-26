package com.example.items.statistics;

import com.example.statistics.AdditionalStatisticsNamesEnum;
import dev.morphia.annotations.ExternalEntity;

import java.util.HashMap;
import java.util.Map;

@ExternalEntity(target = ItemAdditionalStatisticsMap.class)
public non-sealed class ItemAdditionalStatisticsMap implements ItemStatisticsMap {
    private Map<String, ItemStatisticsObject> statisticsMap = new HashMap<>();

    public ItemAdditionalStatisticsMap() {}
    public ItemAdditionalStatisticsMap(Map<String, ItemStatisticsObject> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public Map<String, ItemStatisticsObject> getStatisticsMap() {
        return statisticsMap;
    }
    public void addStatistic(String name, ItemStatisticsObject statistic){
        this.statisticsMap.put(name, statistic);
    }

    @Override
    public String toString() {
        return "ItemAdditionalStatisticsMap{" +
                "statisticsMap=" + statisticsMap +
                '}';
    }

}