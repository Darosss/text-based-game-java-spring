package com.example.users.inventory;

import dev.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private Datastore datastore;

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



}
