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
        return new Item(itemName, "Description of "+itemName, level, value, type, rarity, weight,prefix, suffix, new HashMap<>(), new HashMap<>());
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
    public static <KeyType extends String> Map<KeyType, ItemStatisticsObject> getMergedItemStatisticsObjectMaps(Map<KeyType, ItemStatisticsObject> destination, Map<KeyType, ItemStatisticsObject> source) {
        Map<KeyType, ItemStatisticsObject> newMap = new HashMap<>();
        source.forEach((sourceKey, sourceValue) -> {
            mergeHelper(newMap, sourceKey, sourceValue);
        });
        destination.forEach((sourceKey, destinationValue) -> {
            mergeHelper( newMap, sourceKey, destinationValue);
        });

        return newMap;
    }

    private static <KeyType extends String> void mergeHelper(Map<KeyType, ItemStatisticsObject> mapToMerge, KeyType currentKey, ItemStatisticsObject currentValue) {
        if (mapToMerge.containsKey(currentKey)) {
            ItemStatisticsObject mapToMergeVal = mapToMerge.get(currentKey);
            int summedValue = currentValue.getValue() + mapToMergeVal.getValue();
            float summedPercentageValue = currentValue.getPercentageValue() + mapToMergeVal.getPercentageValue();

            mapToMerge.put(currentKey,
                    new ItemStatisticsObject(currentKey, summedValue, summedPercentageValue)
            );
        } else {
            mapToMerge.put(currentKey, new ItemStatisticsObject(
                    currentValue.getName(), currentValue.getValue(), currentValue.getPercentageValue()
            ));
        }
    }
}
