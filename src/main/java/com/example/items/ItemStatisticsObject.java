package com.example.items;

import com.example.statistics.StatisticsNamesEnum;

public class ItemStatisticsObject {
    private StatisticsNamesEnum name;
    private Integer value;
    private ValueType valueType;


    public ItemStatisticsObject(StatisticsNamesEnum name, Integer value, ValueType valueType) {
        this.name = name;
        this.value = value;
        this.valueType = valueType;
    }

    public StatisticsNamesEnum getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public enum ValueType {
        ABSOLUTE, PERCENTAGE
    }
}
