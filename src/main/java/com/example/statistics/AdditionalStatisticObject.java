package com.example.statistics;

public class AdditionalStatisticObject extends StatisticObject<AdditionalStatisticsNamesEnum> {

    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name, int value) {
        super(name, value);
    }
    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name) {
        super(name, 0);

    }
    public AdditionalStatisticObject() {}


}
