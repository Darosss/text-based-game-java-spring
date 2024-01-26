package com.example.items;

import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Item create(Item item) {
        return datastore.save(item);

    };
    public List<Item> create(List<Item> item) {
        return datastore.save(item);
    };

    public Optional<Item> findItemByItemType(ItemTypeEnum type) {
        return Optional.ofNullable(datastore.find(Item.class).filter(Filters.eq("type", type)).first());
    }

    public void removeAllItems(){
        datastore
                .find(Item.class)
                .delete(new DeleteOptions().multi(true));
    }
}
