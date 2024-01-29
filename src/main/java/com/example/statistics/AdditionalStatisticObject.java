package com.example.statistics;

import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target =AdditionalStatisticObject.class)
public class AdditionalStatisticObject extends CharacterStatisticObject<AdditionalStatisticsNamesEnum> {
    private int baseStatBonus = 0;
    public AdditionalStatisticObject() {super();}
    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name, int value) {
        super(name, value);
    }
    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name) {
        this(name, 0);
    }


    protected int getCalculatedEffectiveValue() {
        return super.getCalculatedEffectiveValue(
                this.getValue() + this.getBonus() + this.baseStatBonus
        );
    }

    @Override
    protected void updateEffectiveValue() {
        this.setEffectiveValue(this.getCalculatedEffectiveValue());
    }
    public void setBaseStatBonus(int baseStatBonus) {
        this.baseStatBonus = baseStatBonus;
        this.updateEffectiveValue();
    }

    public int getBaseStatBonus() {
        return baseStatBonus;
    }


    @Override
    public String toString() {
        return "AdditionalStatisticObject{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
