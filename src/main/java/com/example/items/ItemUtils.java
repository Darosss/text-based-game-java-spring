package com.example.items;

import com.example.items.statistics.*;
import com.example.utils.RandomUtils;
import org.springframework.data.util.Pair;

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

    public static int getItemValueBasedOnRarityLevel(int level, ItemRarityEnum rarity) {
        double BASE_ITEM_COST = 100.0; double BASE_ITEM_COST_PER_LEVEL = 50;
        double itemValue = BASE_ITEM_COST + (level * BASE_ITEM_COST_PER_LEVEL);

        for(ItemRarityEnum value: ItemRarityEnum.values()){
            itemValue += itemValue * value.getBonusCostValue();
            if(rarity.equals(value)) break;
        }

        return (int) itemValue;
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

    public static int getConsumableItemHpGain(int level, ItemRarityEnum rarity, ItemsSubtypes subtype){
        double hpGain = subtype.getHealthGainPerLevel() * level;
        for(ItemRarityEnum value: ItemRarityEnum.values()){
            hpGain += hpGain * (value.getBonusValue()/100);
            if(rarity.equals(value)) break;
        }

        return (int) hpGain;
    }

    public static Item generateItemWithoutBaseStats(
            String itemName, ItemTypeEnum type, ItemsSubtypes subtype, int level, ItemRarityEnum rarity, ItemPrefixesEnum prefix, ItemSuffixesEnum suffix
    ) {
        return generateItem(itemName, type, subtype, level, rarity, prefix,suffix, new HashMap<>(), new HashMap<>());
    }

    private static Item generateItem(
            String itemName, ItemTypeEnum type, ItemsSubtypes subtype, int level,  ItemRarityEnum rarity,
            ItemPrefixesEnum prefix, ItemSuffixesEnum suffix,
            Map<String, ItemStatisticsObject> baseStatistics,
            Map<String, ItemStatisticsObject> baseAdditionalStatistics
    ){

        Pair<Float, Float> weightRange = subtype.getWeightRange();
        float itemWeight = RandomUtils.getRandomValueWithinRange(weightRange.getFirst(), weightRange.getSecond());
        int itemValue = getItemValueBasedOnRarityLevel(level, rarity);
        switch (type){
            case CONSUMABLE -> {
                return new ItemConsumable(itemName, "Description of "+itemName,
                        level, itemValue,
                        rarity, itemWeight, subtype);
            }
            case MERCENARY -> {
                return new ItemMercenary(itemName, "Description of "+itemName,
                        level, getItemValueBasedOnRarityLevel(level, rarity), rarity, itemWeight, subtype,
                        //TODO: add stats for mercenaries
                        baseStatistics, baseAdditionalStatistics);
            }
        }
        return new ItemWearable(itemName, "Description of "+itemName,
                level, getItemValueBasedOnRarityLevel(level, rarity), type,
                subtype, rarity, itemWeight,prefix, suffix, new HashMap<>(), new HashMap<>());

    }

    public static Item generateRandomItemWithoutBaseStats(String name, int itemLevel, ItemTypeEnum itemType){
        ItemsSubtypes subtype = RandomUtils.getRandomItemFromArray(itemType.getSubtypes());
        return  generateItemWithoutBaseStats(name, itemType, subtype, itemLevel,
                getRandomRarityItem(), getRandomItemPrefix(), getRandomItemSuffix()
        );
    };

    //NOTE: That's for debug right now;
    public static Item generateRandomItem(String name, int itemLevel, ItemTypeEnum itemType,
                                          Map<String, ItemStatisticsObject> baseStatistics,
                                          Map<String, ItemStatisticsObject> baseAdditionalStatistics
    ){
        ItemsSubtypes subtype = RandomUtils.getRandomItemFromArray(itemType.getSubtypes());
        return generateItem(name, itemType, subtype, itemLevel,
                getRandomRarityItem(),getRandomItemPrefix(),
                getRandomItemSuffix(), baseStatistics, baseAdditionalStatistics
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
