package com.example.items;

import com.example.items.statistics.ItemAdditionalStatisticsMap;
import com.example.items.statistics.ItemBaseStatisticsMap;
import com.example.items.statistics.ItemStatisticsObject;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//CONTROLLER FOR DEBUGGING
//EASILY ADD RANDOM ITEMS
//ALL METHODS PUBLIC FOR EASY DEBUG ACCESS
@RestController
public class ItemDebugDataGeneratorController {
    private ItemService service;

    @Autowired
    public ItemDebugDataGeneratorController(ItemService service) {
        this.service = service;
    }

    public ItemStatisticsObject.ValueType getRandomItemValueType() {
        ItemStatisticsObject.ValueType[] values = ItemStatisticsObject.ValueType.values();
        Random random = new Random();
        return values[random.nextInt(values.length)];
    }


    public ItemBaseStatisticsMap generateRandomBaseStats(int count) {
        ItemBaseStatisticsMap baseStats = new ItemBaseStatisticsMap();
        BaseStatisticsNamesEnum[] values = BaseStatisticsNamesEnum.values();
        List<BaseStatisticsNamesEnum> list = Arrays.asList(values);
        Collections.shuffle(list);
        values = list.toArray(new BaseStatisticsNamesEnum[0]);

        for (int i = 0; i < Math.min(count, values.length); i++){
            ItemStatisticsObject statToAdd =
                    new ItemStatisticsObject(
                            values[i].getDisplayName(),
                            RandomUtils.getRandomValueWithinRange(1,150),
                            getRandomItemValueType());

            baseStats.addStatistic(values[i].getDisplayName(), statToAdd);
        }

        return baseStats;
    }

    public ItemAdditionalStatisticsMap generateRandomAdditionalBaseStats(int count) {
        ItemAdditionalStatisticsMap additionalStats = new ItemAdditionalStatisticsMap();
        AdditionalStatisticsNamesEnum[] values = AdditionalStatisticsNamesEnum.values();
        List<AdditionalStatisticsNamesEnum> list = Arrays.asList(values);
        Collections.shuffle(list);
        values = list.toArray(new AdditionalStatisticsNamesEnum[0]);

        for (int i = 0; i < Math.min(count, values.length); i++){
            ItemStatisticsObject statToAdd =new ItemStatisticsObject(
                    values[i].getDisplayName(),
                    RandomUtils.getRandomValueWithinRange(1,150),
                    getRandomItemValueType());
            additionalStats.addStatistic(values[i], statToAdd);
        }

        return additionalStats;
    }

    public ItemTypeEnum getRandomItemType() {

        Random random = new Random();
        ItemTypeEnum[] values =ItemTypeEnum.values();
        return values[random.nextInt(values.length)];
    }

    public String getRandomItemName() {
        String[] itemNames = {
                "Sword", "Shield", "Potion", "Armor", "Staff",
                "Ring", "Amulet", "Scroll", "Bow", "Dagger",
                "Helmet", "Gauntlets", "Boots", "Cloak", "Wand",
                "Elixir", "Gloves", "Tome", "Quiver", "Bracelet",
                "Goggles", "Scepter", "Cape", "Spear", "Necklace",
                "Robe", "Trinket", "Hammer", "Grimoire", "Talisman"
        };

        Random random = new Random();
        int randomIndex = random.nextInt(itemNames.length);

        // Get the random item
        String randomItemName = itemNames[randomIndex];

        return randomItemName;
    }

    public ItemRarityEnum getRandomRarityItem() {
        Random random = new Random();
        ItemRarityEnum[] values =ItemRarityEnum.values();
        return values[random.nextInt(values.length)];
    }

    @GetMapping("items/")
    public List<Item> getItems(){
        return service.findAll();
    }

    @PostMapping("items/debug/create-with-random-data/{countOfItems}/{baseStatsCount}/{additionalStatsCount}")
    public void generateItemWithRandomData(@PathVariable int countOfItems,
                                           @PathVariable int baseStatsCount,
                                           @PathVariable int additionalStatsCount
                                           ){
        for (int i = 0; i<= countOfItems; i ++){
            service.create(getRandomItemName(), "desc",
                    RandomUtils.getRandomValueWithinRange(1,100), RandomUtils.getRandomValueWithinRange(100,10000),
                    getRandomItemType(), getRandomRarityItem(), RandomUtils.getRandomFloatValueWithinRange(0.1f, 100f), generateRandomBaseStats(baseStatsCount), generateRandomAdditionalBaseStats(additionalStatsCount) );

        }


    }

    @DeleteMapping("items/debug/delete-all")
    public void deleteAllItems() {
        service.removeAllItems();
    }
}
