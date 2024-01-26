package com.example.statistics;

import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target =AdditionalStatisticObject.class)
public class AdditionalStatisticObject extends StatisticObject<AdditionalStatisticsNamesEnum> {
    private int bonus = 0;
    private float bonusPercentage = 0;
    private int effectiveValue;

    public AdditionalStatisticObject() {}
    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name, int value) {
        super(name, value);
    }
    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name) {
        super(name, 0);

    }

    public void addToBonusesAndCalculateEffVal (int bonus, float bonusPercentage) {
        this.bonus += bonus;
        this.bonusPercentage += bonusPercentage;

        this.updateEffectiveValue();
    }

    public void subtractFromBonusesAndCalculateEffVal (int bonus, float bonusPercentage) {
        this.bonus -= bonus;
        this.bonusPercentage -= bonusPercentage;

        this.updateEffectiveValue();

    }

    private void updateEffectiveValue() {
        int baseAndBonus = value + bonus;
        float percentageAdjust = (bonusPercentage / 100f);

        float calculatedPercentageValue =  ((value + bonus) * percentageAdjust);
        if(baseAndBonus < 0 && percentageAdjust < 0) calculatedPercentageValue = -calculatedPercentageValue;

        this.effectiveValue = (int)calculatedPercentageValue + baseAndBonus ;
    }

    public int getEffectiveValue() {
        return effectiveValue;
    }

    public float getBonusPercentage() {
        return bonusPercentage;
    }

    public void setBonusPercentage(float bonusPercentage) {
        this.bonusPercentage = bonusPercentage;
    }

    public void setEffectiveValue(int effectiveValue) {
        this.effectiveValue = effectiveValue;
    }
}
