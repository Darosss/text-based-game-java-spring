package com.example.items;

import com.example.items.statistics.ItemAdditionalStatisticsMap;
import com.example.items.statistics.ItemBaseStatisticsMap;
import com.example.users.User;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
@Service
public class ItemService {
    private final Datastore datastore;


    @Autowired
    public ItemService(Datastore datastore) {
        this.datastore = datastore;
    }

    public Optional<Item> findOne(String id) {
        return Optional.ofNullable(datastore.find(Item.class)
                .filter(Filters.eq("id", new ObjectId(id))).first());
    }

    public List<Item> findAll() {
        return datastore.find(Item.class).stream().toList();
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
        return datastore.save(newItem);

    };

    public Optional<Item> findItemByItemType(ItemTypeEnum type) {
        return Optional.ofNullable(datastore.find(Item.class).filter(Filters.eq("type", type)).first());
    }

    public void removeAllItems(){
        datastore.delete(Item.class);
    }
}
