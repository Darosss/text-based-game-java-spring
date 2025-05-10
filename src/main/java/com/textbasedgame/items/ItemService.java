package com.textbasedgame.items;

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
    public <T extends Item> Optional<T> findOne(String id, Class<T> itemClass) {
        return Optional.ofNullable(datastore.find(itemClass)
                .filter(Filters.eq("id", new ObjectId(id)))
                .first());
    }
    public List<Item> findAll() {
        return datastore.find(Item.class).stream().toList();
    }

    public <T extends Item> T create(T item) {
        return datastore.save(item);
    };
    public <T extends Item> List<T> create(List<T> item) {
        return datastore.save(item);
    };

    public void removeAllItems(){
        datastore
                .find(Item.class)
                .delete(new DeleteOptions().multi(true));
    }
}
