package com.example.characters;

import com.example.statistics.BaseStatisticsNamesEnum;

public class CharacterStatisticObject {
    protected BaseStatisticsNamesEnum name;
    protected int basic;
    protected int bonus;
    protected int max;

    public BaseStatisticsNamesEnum getName() { return name; }

    public int getCurrentValue() {return basic + bonus; }
    public void setName(BaseStatisticsNamesEnum name) { this.name = name; }
    public int getBasic() {
        return basic;
    }

    public void setBasic(int basic) {
        this.basic = basic;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public CharacterStatisticObject(int basic) {
        this.basic = basic;
        this.bonus = 0;
        this.max = basic * 2;
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
