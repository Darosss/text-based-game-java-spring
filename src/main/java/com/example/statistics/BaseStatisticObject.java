package com.example.statistics;


import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target = BaseStatisticObject.class)
public class BaseStatisticObject extends  CharacterStatisticObject<BaseStatisticsNamesEnum>{
    private int max = 0;

    public BaseStatisticObject() {}

    public BaseStatisticObject(BaseStatisticsNamesEnum name) {
        super(name, 1);
    }
    public BaseStatisticObject(BaseStatisticsNamesEnum name, int value) {
        super(name, value);
    }

    public void setValue(int newValue) {
        this.updateMaxProportion(newValue);
        super.setValue(newValue);
    }

    protected int getCalculatedEffectiveValue() {
        int calculatedEffective = super.getCalculatedEffectiveValue(
            this.getValue() + this.getBonus()
        );

        return Math.min(calculatedEffective, max);
    }

    @Override
    protected void updateEffectiveValue() {
        this.setEffectiveValue(this.getCalculatedEffectiveValue());
    }

    public int getMax() {
        return max;
    }

    private void updateMaxProportion() {
        this.max = value * 2;
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
