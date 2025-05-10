package com.textbasedgame.users.inventory;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final Datastore datastore;

    @Autowired
    public InventoryService(Datastore datastore) {
        this.datastore = datastore;
    }

    public Inventory create(Inventory inventory) {
        return this.datastore.save(inventory);
    }

    public Inventory update(Inventory inventory) {
        return this.datastore.save(inventory);
    }

    public Inventory getUserInventory(ObjectId userId) {
        return this.datastore.find(Inventory.class).filter(Filters.eq("user", userId)).first();
    }
    public Inventory getUserInventory(String userId) {
        return this.getUserInventory(new ObjectId(userId));
    }

}
