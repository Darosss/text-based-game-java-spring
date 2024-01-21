package com.example.characters;

import com.example.statistics.BaseStatisticsNamesEnum;

public class CharacterStatisticStrengthObject extends CharacterStatisticObject {



    public CharacterStatisticStrengthObject(int basic) {
        super(basic);
    }


    @Override
    public String toString() {
        return "StatisticObject{" +
                "basic=" + basic +
                ", bonus=" + bonus +
                ", max=" + max +
                '}';
    }
}
