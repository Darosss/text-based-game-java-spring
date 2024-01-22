package com.example.items;

import com.example.items.statistics.ItemAdditionalStatisticsMap;
import com.example.items.statistics.ItemBaseStatisticsMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
@Service
public class ItemService {
    @Autowired
    private ItemRepository repository;


    public Optional<Item> findOne(String id) {
        return repository.findById(id);
    }
    public Item create(
            String name, String desc, int level, int value, ItemTypeEnum itemType,
            ItemRarityEnum itemRarity, float weight, ItemBaseStatisticsMap statistics, ItemAdditionalStatisticsMap additionalStats
    ) {
        Item newItem = new Item(
                name, desc, level,
                value, itemType, itemRarity,
                weight,
                statistics, additionalStats
        );
        return repository.save(newItem);

    };

    public Optional<Item> findItemByItemType(ItemTypeEnum type) {
        return this.repository.findByItemType(type);
    }

    public void removeAllItems(){
        repository.deleteAll();
    }
}
