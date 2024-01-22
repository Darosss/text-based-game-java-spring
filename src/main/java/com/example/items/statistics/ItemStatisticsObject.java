package com.example.items.statistics;


import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.statistics.StatisticObject;

import java.util.Map;

public class ItemStatisticsObject<NameType> extends StatisticObject<NameType> {
    private ValueType valueType;


    public ItemStatisticsObject(NameType name, Integer value, ValueType valueType) {
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


