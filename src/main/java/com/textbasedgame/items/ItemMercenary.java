package com.textbasedgame.items;

import com.textbasedgame.items.statistics.ItemStatistics;
import com.textbasedgame.items.statistics.ItemStatisticsObject;
import com.textbasedgame.users.User;

import java.util.Map;

public class ItemMercenary extends Item{
    //TODO: add stats for mercenaries
    // (enums with types of mercenary like: tank, fighter, assassin etc??

    public ItemMercenary(){};

    public ItemMercenary(String name, User user, String description, Integer level,
                         Integer value, ItemRarityEnum rarity, float weight,
                         ItemsSubtypes subtype,
                         Map<String, ItemStatisticsObject> baseStatistics,
                         Map<String, ItemStatisticsObject> baseAdditionalStatistics

    ){
        super(name, user, description, level, value, ItemTypeEnum.MERCENARY, subtype, rarity, weight);
        this.statistics = new ItemStatistics(baseStatistics, baseAdditionalStatistics, level, rarity, this.getSubtype());
    }
}
