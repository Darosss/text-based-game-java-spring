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
        this.updateEffectiveValue();
    }
    public AdditionalStatisticObject(AdditionalStatisticsNamesEnum name) {
        super(name, 0);
        this.updateEffectiveValue();
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

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void updateStatistic(int value, StatisticsUtils.StatisticUpdateType updateType) {
        switch (updateType) {
            case VALUE:
                this.setValue(value);
                break;
            case BONUS:
                this.setBonus(value);
                break;
            case PERCENTAGE_BONUS :
                this.setBonusPercentage(value);
                break;

            default:
                throw new IllegalArgumentException("Unsupported update type: " + updateType);
        }
        this.updateEffectiveValue();
    }
}
