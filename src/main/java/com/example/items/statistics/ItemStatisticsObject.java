package com.example.items.statistics;


import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.statistics.StatisticObject;
import dev.morphia.annotations.ExternalEntity;

import java.util.Map;

@ExternalEntity(target = ItemStatisticsObject.class)
public class ItemStatisticsObject extends StatisticObject<String> {
    private float percentageValue = 0f;
    public ItemStatisticsObject(){};
    public ItemStatisticsObject(String name, Integer value, float percentageValue) {
        super(name, value);
        this.percentageValue = percentageValue;
    }

    public float getPercentageValue() {
        return percentageValue;
    }

    public void setPercentageValue(float percentageValue) {
        this.percentageValue = percentageValue;
    }
}


