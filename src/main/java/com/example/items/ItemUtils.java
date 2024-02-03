package com.example.items;

import com.example.items.statistics.*;
import com.example.utils.RandomUtils;

import java.util.*;

public class ItemUtils {
    private ItemUtils() {}


    public static double getItemLevelFactorValueStatistics(int itemLevel) {
        double ITEM_STATISTIC_FACTOR_PER_LEVEL = 1.5;
        return (ITEM_STATISTIC_FACTOR_PER_LEVEL * itemLevel);
    }
    public static double getItemLeveFactorPercentStatistics(int itemLevel) {
        double ITEM_STATISTIC_PERCENT_FACTOR_PER_LEVEL = 0.25;
        return (ITEM_STATISTIC_PERCENT_FACTOR_PER_LEVEL * itemLevel);
    }
    public static ItemTypeEnum getRandomItemType() {

        Random random = new Random();
        ItemTypeEnum[] values =ItemTypeEnum.values();
        return values[random.nextInt(values.length)];
    }

    public static ItemPrefixesEnum getRandomItemPrefix(){
        return RandomUtils.getRandomItemFromArray( ItemPrefixesEnum.values());

    }
    public static ItemSuffixesEnum getRandomItemSuffix() {
        return RandomUtils.getRandomItemFromArray( ItemSuffixesEnum.values());
    }

    public static String getItemName(ItemTypeEnum type){
        return type.getDisplayName();
    }


    public static ItemRarityEnum getRandomRarityItem(){
        return getRandomRarityItem(1);
    }
    public static ItemRarityEnum getRandomRarityItem(double additionalBonusMultiplier) {
        double randomDouble = RandomUtils.getRandomValueWithinRange(0, 1.0);
        ItemRarityEnum[] values =ItemRarityEnum.values();
        for(int i = values.length - 1; i >= 0; i--){
            ItemRarityEnum currentRarity = values[i];
            double probabilityWithBonus = Math.min(1.0, currentRarity.getProbability() * Math.max(0.1, additionalBonusMultiplier));
            if(randomDouble <= probabilityWithBonus) return currentRarity;
        }

        return ItemRarityEnum.COMMON;
    }



    public static Item generateItemWithoutBaseStats(
            String itemName, ItemTypeEnum type, int level, int value, ItemRarityEnum rarity,
            float weight, ItemPrefixesEnum prefix, ItemSuffixesEnum suffix
    ) {
        return new Item(itemName, "Description of "+itemName, level, value, type, rarity, weight,prefix, suffix);
    }


    //NOTE: That's for debug right now;
    private static Item generateItem(
            String itemName, ItemTypeEnum type, int level, int value, ItemRarityEnum rarity,
            float weight, ItemPrefixesEnum prefix, ItemSuffixesEnum suffix,
            Map<String, ItemStatisticsObject> baseStatistics,
            Map<String, ItemStatisticsObject> baseAdditionalStatistics
    ){

        return new Item(itemName, "Description of "+itemName,
                level, value, type, rarity, weight,prefix, suffix,
                baseStatistics, baseAdditionalStatistics
        );

    }

    public static Item generateRandomItemWithoutBaseStats(String name, int itemLevel, ItemTypeEnum itemType){
        return  generateItemWithoutBaseStats(name, itemType, itemLevel,
                RandomUtils.getRandomValueWithinRange(1,40000),
                getRandomRarityItem(),
                RandomUtils.getRandomValueWithinRange(0.1f,100f),
                getRandomItemPrefix(), getRandomItemSuffix()
        );
    };

    //NOTE: That's for debug right now;
    public static Item generateRandomItem(String name, int itemLevel, ItemTypeEnum itemType,
                                          Map<String, ItemStatisticsObject> baseStatistics,
                                          Map<String, ItemStatisticsObject> baseAdditionalStatistics
    ){
        return generateItem(name, itemType, itemLevel,
                RandomUtils.getRandomValueWithinRange(1,40000),
                getRandomRarityItem(),
                RandomUtils.getRandomValueWithinRange(0.1f,100f),
                getRandomItemPrefix(), getRandomItemSuffix(), baseStatistics, baseAdditionalStatistics
        );
    };


    public static Item generateRandomItem(){
        ItemTypeEnum randomItemType = getRandomItemType();
        String randomItemName = getItemName(randomItemType);
        return generateRandomItemWithoutBaseStats(randomItemName, RandomUtils.getRandomValueWithinRange(1,100), randomItemType);
    }
    public static List<Item> generateRandomItems(int count) {
        List<Item> generatedItems = new ArrayList<>();

        for (int i = 0; i<= count; i ++){
            generatedItems.add(generateRandomItem());
        }
    return generatedItems;
    }

    public static <MapType extends Map<String, ItemStatisticsObject>> MapType mergeItemStatisticsObjectMaps(MapType destination, MapType source) {
        source.forEach((k, v)-> {
            destination.merge(k, v, (v1, v2) -> {
                int summedValue = v1.getValue() + v2.getValue();
                float summedPercentageValue = v1.getPercentageValue() + v2.getPercentageValue();
                return new ItemStatisticsObject(
                        v1.getName(), summedValue, summedPercentageValue
                );
            });
        });

        return destination;
    }


}
