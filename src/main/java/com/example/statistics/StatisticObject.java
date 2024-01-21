package com.example.statistics;

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
}
