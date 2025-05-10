package com.textbasedgame.statistics;


public abstract class StatisticObject<NameType> {
    protected NameType name;
    protected int value;

    public StatisticObject() {}

    public StatisticObject(NameType name, int value) {
        this.name = name;
        this.value = value;
    }

    public NameType getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public void increaseValue(int value) {
        this.setValue(this.value += value);
    }
    public void decreaseValue(int value) {
        this.setValue(this.value -= value);
    }


}
