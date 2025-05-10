package com.textbasedgame.items.statistics;


import com.textbasedgame.statistics.BaseStatisticsNamesEnum;
import com.textbasedgame.statistics.StatisticObject;
import dev.morphia.annotations.ExternalEntity;

import java.util.Map;

@ExternalEntity(target = ItemStatisticsObject.class)
public class ItemStatisticsObject extends StatisticObject<String> {
    private float percentageValue = 0f;
    public ItemStatisticsObject(){};
    public ItemStatisticsObject(String name, Integer value, float percentageValue) {
        super(name, value);
        this.percentageValue = percentageValue;
    }

    public float getPercentageValue() {
        return percentageValue;
    }

    public void setPercentageValue(float percentageValue) {
        this.percentageValue = percentageValue < 0 ? Math.max(-50, percentageValue) : Math.min(percentageValue, 50);
    }

    public void increasePercentageValue(int value) {
        this.setPercentageValue(this.percentageValue + value);
    }

    public void increaseValueByPercent(double percentValue) {
        this.setValue((int)(this.getValue() + (this.getValue() * percentValue)));
    }

    public void increasePercentageValueByPercent(double percentValue) {
        this.setPercentageValue((int)(this.getPercentageValue() + (this.getPercentageValue() * percentValue)));
    }
    public void decreasePercentageValue(int value) {
        this.percentageValue -= value;
    }
}


