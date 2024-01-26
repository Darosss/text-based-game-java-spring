package com.example.items;

import com.example.items.statistics.*;
import com.example.utils.RandomUtils;

import java.util.*;

public class ItemUtils {
    private ItemUtils() {}
//    public static ItemBaseStatisticsMap generateRandomBaseStats(int count) {
//        ItemBaseStatisticsMap baseStats = new ItemBaseStatisticsMap();
//        BaseStatisticsNamesEnum[] values = BaseStatisticsNamesEnum.values();
//        List<BaseStatisticsNamesEnum> list = Arrays.asList(values);
//        Collections.shuffle(list);
//        values = list.toArray(new BaseStatisticsNamesEnum[0]);
//
//        for (int i = 0; i < Math.min(count, values.length); i++){
//            ItemStatisticsObject statToAdd =
//                    new ItemStatisticsObject(
//                            values[i].getDisplayName(),
//                            RandomUtils.getRandomValueWithinRange(1,150),
//                            RandomUtils.getRandomValueWithinRange(1,150)
//                            );
//
//            baseStats.addStatistic(values[i].getDisplayName(), statToAdd);
//        }
//
//        return baseStats;
//    }
//    public static ItemAdditionalStatisticsMap generateRandomAdditionalBaseStats(int count) {
//        ItemAdditionalStatisticsMap additionalStats = new ItemAdditionalStatisticsMap();
//        AdditionalStatisticsNamesEnum[] values = AdditionalStatisticsNamesEnum.values();
//        List<AdditionalStatisticsNamesEnum> list = Arrays.asList(values);
//        Collections.shuffle(list);
//        values = list.toArray(new AdditionalStatisticsNamesEnum[0]);
//
//        for (int i = 0; i < Math.min(count, values.length); i++){
//            ItemStatisticsObject statToAdd =new ItemStatisticsObject(
//                    values[i].getDisplayName(),
//                    RandomUtils.getRandomValueWithinRange(1,150),
//                    getRandomItemValueType());
//            additionalStats.addStatistic(values[i], statToAdd);
//        }
//
//        return additionalStats;
//    }

    public static ItemTypeEnum getRandomItemType() {

        Random random = new Random();
        ItemTypeEnum[] values =ItemTypeEnum.values();
        return values[random.nextInt(values.length)];
    }

    public static ItemPrefixesEnum getRandomItemPrefix(){
        //TODO: Later from some file or db
        return RandomUtils.getRandomItemFromArray( ItemPrefixesEnum.values());

    }
    public static ItemSuffixesEnum getRandomItemSuffix() {
        //TODO: Later from some file or db
        return RandomUtils.getRandomItemFromArray( ItemSuffixesEnum.values());
    }

    public static String getItemName(ItemTypeEnum type){
        return type.getDisplayName();
    }

    public static ItemRarityEnum getRandomRarityItem() {
        Random random = new Random();
        ItemRarityEnum[] values =ItemRarityEnum.values();
        return values[random.nextInt(values.length)];
    }
    private static Item generateItem(
            String itemName, ItemTypeEnum type, int level, int value, ItemRarityEnum rarity,
            float weight, ItemPrefixesEnum prefix, ItemSuffixesEnum suffix
//            ItemBaseStatisticsMap statistics, ItemAdditionalStatisticsMap additionalStatistics
    ){

        return new Item(itemName, "Description of "+itemName,
                level, value, type, rarity, weight,
                prefix, suffix);

    }

    public static List<Item> generateRandomItems(int count) {
        List<Item> generatedItems = new ArrayList<>();

        for (int i = 0; i<= count; i ++){
            ItemTypeEnum randomItemType = getRandomItemType();
            String randomItemName = getItemName(randomItemType);
            generatedItems.add(
                    generateItem(randomItemName, randomItemType,
                            RandomUtils.getRandomValueWithinRange(1,100),
                            RandomUtils.getRandomValueWithinRange(1,40000),
                            getRandomRarityItem(),
                            RandomUtils.getRandomFloatValueWithinRange(0.1f,100f),
                            getRandomItemPrefix(), getRandomItemSuffix()
                            )
            );
        }
    return generatedItems;
    }

    public static <MapType extends Map<String, ItemStatisticsObject>> void mergeItemStatisticsObjectMaps(MapType destination, MapType source) {
        source.forEach((k, v)-> {
            destination.merge(k, v, (v1, v2) -> {
                int summedValue = v1.getValue() + v2.getValue();
                float summedPercentageValue = v1.getPercentageValue() + v2.getPercentageValue();
                return new ItemStatisticsObject(
                        v1.getName(), summedValue, summedPercentageValue
                );
            });
        });
    }
}
