package com.example.items;

import com.example.items.statistics.ItemAdditionalStatisticsMap;
import com.example.items.statistics.ItemBaseStatisticsMap;
import com.example.items.statistics.ItemStatisticsObject;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(collection = "items")
public class Item {

    @Id
    private String id;

    private String userId;
    private String name;

    private String description;

    private Integer level;

    private Integer value;

    private Integer upgradePoints;

    private ItemTypeEnum type;

    private ItemRarityEnum rarity;

    private float weight;
    private ItemBaseStatisticsMap statistics = new ItemBaseStatisticsMap(new HashMap<>());
    private ItemAdditionalStatisticsMap additionalStatistics = new ItemAdditionalStatisticsMap(new HashMap<>());


    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Item(String name, String description, Integer level,
                Integer value, ItemTypeEnum type,
                ItemRarityEnum rarity, float weight,
                ItemBaseStatisticsMap statistics,
                ItemAdditionalStatisticsMap additionalStatistics

    ) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.value = value;
        this.type = type;
        this.rarity = rarity;
        this.statistics = statistics;
        this.additionalStatistics = additionalStatistics;
        this.upgradePoints = 0;
        this.weight = weight;
    }

    public void setUpgradePoints(Integer upgradePoints) {
        this.upgradePoints = upgradePoints;
    }

    public void setRarity(ItemRarityEnum rarity) {
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getUpgradePoints() {
        return upgradePoints;
    }

    public ItemTypeEnum getType() {
        return type;
    }

    public ItemRarityEnum getRarity() {
        return rarity;
    }

    public ItemBaseStatisticsMap getStatistics() {
        return statistics;
    }

    public float getWeight() { return weight; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public ItemAdditionalStatisticsMap getAdditionalStatistics() {
        return additionalStatistics;
    }

    public ItemStatisticsObject<BaseStatisticsNamesEnum> getStatisticsByName(BaseStatisticsNamesEnum name) {
        return statistics.getStatisticsMap().get(name);
    }

    public ItemStatisticsObject<AdditionalStatisticsNamesEnum> getStatisticsByName(AdditionalStatisticsNamesEnum name) {
        return additionalStatistics.getStatisticsMap().get(name);
    }



    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
                ", value=" + value +
                ", upgradePoints=" + upgradePoints +
                ", type=" + type +
                ", rarity=" + rarity +
                ", weight=" + weight +
                ", statistics=" + statistics +
                ", additionalStatistics=" + additionalStatistics +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

