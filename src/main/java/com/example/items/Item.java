package com.example.items;

import com.example.items.statistics.*;
import com.example.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Map;

@Entity("items")
public class Item {

    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;

    @Reference(idOnly = true, lazy = true)
    @JsonIgnore
    private User user;

    private String name;

    private String description;

    private Integer level;

    private Integer value;

    private ItemPrefixesEnum prefix;
    private ItemSuffixesEnum suffix;

    private Integer upgradePoints;

    private ItemTypeEnum type;
    private ItemsSubtypes subtype;

    private int hpGain;
    private ItemRarityEnum rarity;

    private float weight;
    private ItemStatistics statistics;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     Constructor for adding item without basing only on prefix + suffix
     */
    public Item() {}
    public Item(String name, String description, Integer level,
                Integer value, ItemTypeEnum type, ItemsSubtypes subtype,
                ItemRarityEnum rarity, float weight,
                ItemPrefixesEnum prefix, ItemSuffixesEnum suffix,
                Map<String, ItemStatisticsObject> baseStatistics,
                Map<String, ItemStatisticsObject> baseAdditionalStatistics

    ) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.value = value;
        this.prefix = prefix;
        this.suffix = suffix;
        this.type = type;
        this.subtype = subtype;
        this.rarity = rarity;
        if(type.equals(ItemTypeEnum.CONSUMABLE)) this.hpGain = ItemUtils.getConsumableItemHpGain(level, rarity, subtype);
        else this.statistics = new ItemStatistics(baseStatistics, baseAdditionalStatistics, prefix, suffix, level, rarity, subtype);        this.upgradePoints = 0;
        this.weight = weight;
    }

    public ObjectId getId() {
        return id;
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

    public String getNameWithPrefixAndSuffix() {
        return prefix.getDisplayName() + " " + name  + " " + suffix.getDisplayName();
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

    public ItemsSubtypes getSubtype() {
        return subtype;
    }

    public ItemRarityEnum getRarity() {
        return rarity;
    }

    public ItemPrefixesEnum getPrefix() {
        return prefix;
    }

    public ItemSuffixesEnum getSuffix() {
        return suffix;
    }

    public float getWeight() { return weight; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public ItemStatisticsObject getStatisticsByName(String name) {
        return this.statistics.getBaseStatistics().get(name);
    }

    public ItemStatisticsObject getAdditionalStatisticsByName(String name) {
        return this.statistics.getAdditionalStatistics().get(name);
    }

    public ItemStatistics getStatistics() {
        return statistics;
    }

    public int getHpGain() {
        return hpGain;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
                ", value=" + value +
                ", prefix=" + prefix +
                ", suffix=" + suffix +
                ", upgradePoints=" + upgradePoints +
                ", type=" + type +
                ", rarity=" + rarity +
                ", weight=" + weight +
                ", statistics=" + statistics +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

