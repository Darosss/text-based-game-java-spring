package com.example.statistics;

import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target =AdditionalStatisticObject.class)
public class AdditionalStatisticObject extends StatisticObject<AdditionalStatisticsNamesEnum> {

    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name, int value) {
        super(name, value);
    }
    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name) {
        super(name, 0);

    }
    public AdditionalStatisticObject() {}


}
