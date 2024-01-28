package com.example.statistics;


import dev.morphia.annotations.ExternalEntity;

@ExternalEntity(target = BaseStatisticObject.class)
public class BaseStatisticObject extends  StatisticObject<BaseStatisticsNamesEnum>{
    private int bonus = 0;
    private float bonusPercentage = 0;

    private int effectiveValue;
    private int max;

    public BaseStatisticObject() {}

    public BaseStatisticObject(BaseStatisticsNamesEnum name) {
        super(name, 5);
        this.max = value * 2;
        this.updateEffectiveValue();
    }

    public BaseStatisticObject(BaseStatisticsNamesEnum name, int value) {
        super(name, value);
        this.max = value * 2;
        this.updateEffectiveValue();
    }



    @Override
    public void setValue(int newValue) {
        super.setValue(newValue);
        this.max=newValue * 2;
        this.updateEffectiveValue();
    }

    public void addToValue(int value){
        this.value += value;
        this.max = this.value * 2;
        this.updateEffectiveValue();
    }
    public void subtractFromValue(int value){
        this.value -= value;
        this.max = this.value * 2;
        this.updateEffectiveValue();
    }

    public int getEffectiveValue (){
        return effectiveValue;
    }


    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setEffectiveValue(int effectiveValue) {
        this.effectiveValue = effectiveValue;
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

        int calculatedEffective = (int) calculatedPercentageValue + baseAndBonus;
        this.effectiveValue = Math.min(calculatedEffective, max);
    }
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public float getBonusPercentage() {
        return bonusPercentage;
    }

    public void setBonusPercentage(float bonusPercentage) {
        this.bonusPercentage = bonusPercentage;
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
