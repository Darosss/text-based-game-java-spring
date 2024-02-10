package com.example.enemies;

import com.example.items.Item;
import com.example.items.ItemTypeEnum;
import com.example.items.ItemUtils;
import com.example.skirmishes.EnemySkirmishDifficulty;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnemyUtils {
    public record LevelRange(int min, int max){}

    public record ItemProbabilityLoot(int percent, int minItems, int maxItems){};
    public static LevelRange getEnemyLevelRanges(EnemySkirmishDifficulty difficulty, int characterLevel){
        return switch (difficulty){
            case NOOB -> new LevelRange(1, characterLevel/2);
            case WEAKER -> new LevelRange(Math.max(1, characterLevel-10), Math.max(1, characterLevel-2) );
            case EQUAL -> new LevelRange(characterLevel, characterLevel);
            case FAIR -> new LevelRange(Math.max(1, characterLevel-1),Math.max(1, characterLevel+1) );
            case STRONGER -> new LevelRange(characterLevel+5, characterLevel+10);
            case IMPOSSIBLE -> new LevelRange(characterLevel+10, characterLevel*2);
        };
    }

    public static double getEnemyStatsMultiplier(EnemyType type){
        return switch (type){
            case COMMON -> 0.5;
            case UNCOMMON -> 1;
            case RARE -> 1.2;
            case EPIC -> 2;
            case BOSS -> 3;
            case ANCESTOR -> 5;
        };
    }

    public static Enemy createRandomEnemy(String name, int level, EnemyType enemyType,
                      Map<BaseStatisticsNamesEnum, Integer> baseStatistics,
                      Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics
    ){
        return new Enemy(name, level,enemyType, baseStatistics, additionalStatistics);
    }

    public static Enemy createRandomEnemyBasedOnLevel(String name, int level, EnemyType type, double statsMultiplier){
        int multipliedStat = (int) (level * statsMultiplier);
        return createRandomEnemy(name, level, type,
                Map.of(
                        BaseStatisticsNamesEnum.STRENGTH, multipliedStat,
                        BaseStatisticsNamesEnum.DEXTERITY, multipliedStat,
                        BaseStatisticsNamesEnum.CHARISMA, multipliedStat,
                        BaseStatisticsNamesEnum.CONSTITUTION, multipliedStat,
                        BaseStatisticsNamesEnum.INTELLIGENCE, multipliedStat,
                        BaseStatisticsNamesEnum.LUCK, multipliedStat
                ),
                Map.of(
                        AdditionalStatisticsNamesEnum.INITIATIVE, multipliedStat,
                        AdditionalStatisticsNamesEnum.MAX_HEALTH, multipliedStat
                )
        );
    }


    public static EnemyType getEnemyTypeDependsOnProbability() {
        return getEnemyTypeDependsOnProbability(1);
    }
    public static EnemyType getEnemyTypeDependsOnProbability(double additionalBonusMultiplier) {
        double randomDouble = RandomUtils.getRandomValueWithinRange(0, 1.0);
        for(EnemyType value: EnemyType.values()){
            double probabilityWithBonus = Math.min(1.0, value.getProbability() * Math.max(0.1, additionalBonusMultiplier));
            if(randomDouble <= probabilityWithBonus) return value;

        }

        return EnemyType.COMMON;
    }

    public static EnemyType getEnemyTypeBasedOnSkirmishDifficulty(EnemySkirmishDifficulty difficulty){
        return getEnemyTypeDependsOnProbability(getMultiplierForEnemyType(difficulty));
    }

    private static double getMultiplierForEnemyType(EnemySkirmishDifficulty difficulty){
        return switch (difficulty){
            case NOOB ->  2.0;
            case WEAKER -> 0.8;
            case EQUAL -> 1.1;
            case FAIR ->  1.5;
            case STRONGER -> 2.2;
            case IMPOSSIBLE -> 3;
        };
    }

    private static ItemProbabilityLoot getItemProbabilityLootByEnemyType(EnemyType type){
        return switch (type){
            case COMMON -> new ItemProbabilityLoot(15, 1, 1);
            case UNCOMMON -> new ItemProbabilityLoot(28, 1, 1);
            case RARE -> new ItemProbabilityLoot(42, 1, 2);
            case EPIC -> new ItemProbabilityLoot(60, 1, 2);
            case BOSS -> new ItemProbabilityLoot(100, 2, 3);
            case ANCESTOR -> new ItemProbabilityLoot(100, 3, 6);
        };
    }

    public static List<Item> checkLootFromEnemy(EnemyType type, int enemyLevel){
        List<Item> items = new ArrayList<>();
        ItemProbabilityLoot itemProbData = getItemProbabilityLootByEnemyType(type);

        int numberOfItems = RandomUtils.getRandomValueWithinRange(itemProbData.minItems(), itemProbData.maxItems());
        for (int i = 0; i< numberOfItems; i++){
            int itemLevel = RandomUtils.getRandomValueWithinRange(Math.max(1, enemyLevel - 5), enemyLevel + 8);
            ItemTypeEnum itemType = ItemUtils.getRandomItemType();
            //TODO: make random name generator for items
            items.add(ItemUtils.generateRandomItemWithoutBaseStats(
                    itemType + " from enemy: "+type+" | level: "+enemyLevel, itemLevel, itemType)
            );
        }

        for (Item item : items) {
            System.out.println("Looted item from enemy: | level:" + item.getLevel() +" | name:"+  item.getName());
        }
        return items;
    }
}