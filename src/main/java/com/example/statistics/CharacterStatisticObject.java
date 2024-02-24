package com.example.statistics;

public class CharacterStatisticObject<NameType> extends StatisticObject<NameType> {
    private int bonus = 0;
    private float bonusPercentage = 0;
    private int effectiveValue = 0;


    public CharacterStatisticObject(){};
    public CharacterStatisticObject(NameType name){
        super(name, 0);
        this.updateEffectiveValue();
    }

    public CharacterStatisticObject(NameType name, int value){
        super(name, value);
        this.updateEffectiveValue();
    }

    protected int getCalculatedEffectiveValue(int valueToCalculate) {
        float percentageAdjust = (bonusPercentage / 100f);

        float calculatedPercentageValue =  ((value + bonus) * percentageAdjust);
        if(valueToCalculate < 0 && percentageAdjust < 0) calculatedPercentageValue = -calculatedPercentageValue;

        return (int) calculatedPercentageValue + valueToCalculate;
    }

    protected void updateEffectiveValue(){
        setEffectiveValue(getCalculatedEffectiveValue(
                this.bonus + this.value
        ));
    }

    public int getBonus() {
        return bonus;
    }

    public float getBonusPercentage() {
        return bonusPercentage;
    }

    public int getEffectiveValue() {
        return effectiveValue;
    }


    protected void setBonus(int bonus) {
        this.bonus = bonus;
        this.updateEffectiveValue();
    }

    protected void setBonusPercentage(float bonusPercentage) {
        this.bonusPercentage = bonusPercentage;
        this.updateEffectiveValue();
    }

    @Override
    public void increaseValue(int value) {
        super.increaseValue(value);
        this.updateEffectiveValue();
    }

    @Override
    public void decreaseValue(int value) {
        super.decreaseValue(value);
        this.updateEffectiveValue();
    }

    public void increaseBonus(int value) {
        this.bonus += value;
        this.updateEffectiveValue();
    }
    public void increaseBonusPercentage(float value) {
        this.bonusPercentage += value;
        this.updateEffectiveValue();
    }
    public void decreaseBonus(int value) {
        this.bonus -= value;
        this.updateEffectiveValue();
    }
    public void decreaseBonusPercentage(float value) {
        this.bonusPercentage -= value;
        this.updateEffectiveValue();
    }

    @Override
    public void setValue(int newValue) {
        super.setValue(newValue);
        this.updateEffectiveValue();
    }

    public void setEffectiveValue(int effectiveValue) {
        this.effectiveValue = effectiveValue;
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
    }
    public void increaseStatistic(int value, StatisticsUtils.StatisticUpdateType updateType) {
        switch (updateType) {
            case VALUE:
                this.increaseValue(value);
                break;
            case BONUS:
                this.increaseBonus(value);
                break;
            case PERCENTAGE_BONUS :
                this.increaseBonusPercentage(value);
                break;

            default:
                throw new IllegalArgumentException("Unsupported update type: " + updateType);
        }
    }
    public void decreaseStatistic(int value, StatisticsUtils.StatisticUpdateType updateType) {
        switch (updateType) {
            case VALUE:
                this.decreaseValue(value);
                break;
            case BONUS:
                this.decreaseBonus(value);
                break;
            case PERCENTAGE_BONUS :
                this.decreaseBonusPercentage(value);
                break;
            default:
                throw new IllegalArgumentException("Unsupported update type: " + updateType);
        }
    }

    @Override
    public String toString() {
        return "CharacterStatisticObject{" +
                "bonus=" + bonus +
                ", bonusPercentage=" + bonusPercentage +
                ", effectiveValue=" + effectiveValue +
                ", name=" + name +
                ", value=" + value +
                '}';
    }
}
