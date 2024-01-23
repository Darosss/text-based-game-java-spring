package com.example.items.statistics;

import com.example.statistics.BaseStatisticsNamesEnum;
import dev.morphia.annotations.ExternalEntity;

import java.util.HashMap;
import java.util.Map;

@ExternalEntity(target = ItemBaseStatisticsMap.class)
public non-sealed class ItemBaseStatisticsMap implements ItemStatisticsMap {
    private Map<String, ItemStatisticsObject> statisticsMap = new HashMap<>();

    public ItemBaseStatisticsMap() {}
    public ItemBaseStatisticsMap(Map<String, ItemStatisticsObject> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public void setStatisticsMap(Map<String, ItemStatisticsObject> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public void addStatistic(String name, ItemStatisticsObject statistic){
        this.statisticsMap.put(name, statistic);
    }

    public Map<String, ItemStatisticsObject> getStatisticsMap() {
        return statisticsMap;
    }

    @Override
    public String toString() {
        return "ItemBaseStatisticsMap{" +
                "statisticsMap=" + statisticsMap +
                '}';
    }
}

