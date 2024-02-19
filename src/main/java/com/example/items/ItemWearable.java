package com.example.items;

import com.example.items.statistics.ItemPrefixesEnum;
import com.example.items.statistics.ItemStatistics;
import com.example.items.statistics.ItemStatisticsObject;
import com.example.items.statistics.ItemSuffixesEnum;

import java.util.Map;

public class ItemWearable extends Item{
    private ItemPrefixesEnum prefix;
    private ItemSuffixesEnum suffix;

    public ItemWearable() {};

    public ItemWearable(String name, String description, Integer level,
                Integer value, ItemTypeEnum type, ItemsSubtypes subtype,
                ItemRarityEnum rarity, float weight,
                ItemPrefixesEnum prefix, ItemSuffixesEnum suffix,
                Map<String, ItemStatisticsObject> baseStatistics,
                Map<String, ItemStatisticsObject> baseAdditionalStatistics

    ){
        super(name, description, level, value, type, subtype, rarity, weight);
        this.prefix = prefix;
        this.suffix = suffix;
        this.statistics = new ItemStatistics(baseStatistics, baseAdditionalStatistics, prefix, suffix, level, rarity, subtype);

    }

    public String getNameWithPrefixAndSuffix() {
        return prefix.getDisplayName() + " " + this.getName()  + " " + suffix.getDisplayName();
    }

    public ItemPrefixesEnum getPrefix() {
        return prefix;
    }

    public ItemSuffixesEnum getSuffix() {
        return suffix;
    }
}
