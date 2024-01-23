package com.example.items.statistics;


import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.statistics.StatisticObject;
import dev.morphia.annotations.ExternalEntity;

import java.util.Map;

@ExternalEntity(target = ItemStatisticsObject.class)
public class ItemStatisticsObject extends StatisticObject<String> {
    private ValueType valueType;
    public ItemStatisticsObject(){};
    public ItemStatisticsObject(String name, Integer value, ValueType valueType) {
        super(name, value);
        this.valueType = valueType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public enum ValueType {
        ABSOLUTE, PERCENTAGE
    }
}


