package com.example.statistics;

public class BaseStatisticObject extends  StatisticObject<BaseStatisticsNamesEnum>{
    private int bonus = 0;
    private int max;

    public BaseStatisticObject() {}

    public BaseStatisticObject(BaseStatisticsNamesEnum name) {
        super(name, 5);
        this.max = value * 2;
    }

    public BaseStatisticObject(BaseStatisticsNamesEnum name, int value) {
        super(name, value);
        this.max = value * 2;
    }

    @Override
    public void setValue(int newValue) {
        super.setValue(newValue);
        this.max=newValue * 2;
    }

    public int getEffectiveValue (){
        int effectiveValue = value + bonus;
        return Math.min(effectiveValue, max);
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

    @Override
    public String toString() {
        return "BaseStatisticObject{" +
                "bonus=" + bonus +
                ", max=" + max +
                ", name=" + name +
                ", value=" + value +
                '}';
    }
}
