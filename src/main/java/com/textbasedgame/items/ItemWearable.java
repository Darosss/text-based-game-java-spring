package com.textbasedgame.items;

import com.textbasedgame.items.statistics.ItemPrefixesEnum;
import com.textbasedgame.items.statistics.ItemStatistics;
import com.textbasedgame.items.statistics.ItemStatisticsObject;
import com.textbasedgame.items.statistics.ItemSuffixesEnum;
import com.textbasedgame.users.User;

import java.util.Map;

public class ItemWearable extends Item{
    private ItemPrefixesEnum prefix;
    private ItemSuffixesEnum suffix;

    public ItemWearable() {};

    public ItemWearable(String name, User user, String description, Integer level,
                        Integer value, ItemTypeEnum type, ItemsSubtypes subtype,
                        ItemRarityEnum rarity, float weight,
                        ItemPrefixesEnum prefix, ItemSuffixesEnum suffix,
                        Map<String, ItemStatisticsObject> baseStatistics,
                        Map<String, ItemStatisticsObject> baseAdditionalStatistics

    ){
        super(name, user, description, level, value, type, subtype, rarity, weight);
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
