package com.example.items;

import com.example.items.statistics.ItemPrefixesEnum;
import com.example.items.statistics.ItemStatistics;
import com.example.items.statistics.ItemStatisticsObject;
import com.example.items.statistics.ItemSuffixesEnum;
import dev.morphia.annotations.ExternalEntity;

import java.util.Map;

@ExternalEntity(target = Item.class, discriminator = "Item")
public class ItemConsumable extends  Item {
    private final int hpGain;
    public ItemConsumable(String name, String description, Integer level,
                          Integer value, ItemRarityEnum rarity, float weight,
                          ItemsSubtypes subtype
    ){
        super(name, description, level, value, ItemTypeEnum.CONSUMABLE, subtype, rarity, weight);
        this.hpGain = ItemUtils.getConsumableItemHpGain(level, rarity, this.getSubtype());
    }

    /**
     * TODO: Later for potions with debuffs, buffs
     */
//    public ItemConsumable(String name, String description, Integer level,
//                          Integer value, ItemRarityEnum rarity, float weight,
//                          ItemsSubtypes subtype,
//                          Map<String, ItemStatisticsObject> baseStatistics,
//                          Map<String, ItemStatisticsObject> baseAdditionalStatistics
//
//    ){
//        super(name, description, level, value, ItemTypeEnum.CONSUMABLE, subtype, rarity, weight);
//        this.hpGain = ItemUtils.getConsumableItemHpGain(level, rarity, this.getSubtype());
//
//    }

    public int getHpGain() {
        return hpGain;
    }
}
