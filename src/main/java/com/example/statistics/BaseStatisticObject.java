package com.example.statistics;


import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target = BaseStatisticObject.class)
public class BaseStatisticObject extends  CharacterStatisticObject<BaseStatisticsNamesEnum>{
    private int max = 0;

    public BaseStatisticObject() {}

    public BaseStatisticObject(BaseStatisticsNamesEnum name) {
        super(name, 1);
        this.updateMaxProportion();
        //TODO: In super class we call it too improve later
        this.updateEffectiveValue();
    }
    public BaseStatisticObject(BaseStatisticsNamesEnum name, int value) {
        super(name, value);
        this.updateMaxProportion();
        //TODO: In super class we call it too improve later
        this.updateEffectiveValue();

    }

    @Override
    public void setValue(int newValue) {
        this.updateMaxProportion(newValue);
        super.setValue(newValue);
    }

    @Override
    public void increaseValue(int value) {
        this.updateMaxProportion(this.value + value);
        super.increaseValue(value);
    }

    @Override
    public void decreaseValue(int value) {
        this.updateMaxProportion(this.value - value);
        super.decreaseValue(value);
    }

    @Override
    public void updateStatistic(int value, StatisticsUtils.StatisticUpdateType updateType) {
        super.updateStatistic(value, updateType);
    }

    @Override
    public void increaseStatistic(int value, StatisticsUtils.StatisticUpdateType updateType) {
        super.increaseStatistic(value, updateType);
    }

    @Override
    public void decreaseStatistic(int value, StatisticsUtils.StatisticUpdateType updateType) {
        if(updateType.equals(StatisticsUtils.StatisticUpdateType.VALUE)) this.updateMaxProportion(this.value - value);
        super.decreaseStatistic(value, updateType);
    }

    protected int getCalculatedEffectiveValue() {
        int calculatedEffective = super.getCalculatedEffectiveValue(
            this.getValue() + this.getBonus()
        );
        if(calculatedEffective > this.max) return this.max;

        return Math.max(1, calculatedEffective);
    }

    @Override
    protected void updateEffectiveValue() {
        this.setEffectiveValue(this.getCalculatedEffectiveValue());
    }

    public int getMax() {
        return max;
    }

    private void updateMaxProportion() {
        this.max = this.value * 2;
    }
    private void updateMaxProportion(int newValue) {
        this.max = newValue * 2;
    }

    @Override
    public String toString() {
        return "BaseStatisticObject{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
