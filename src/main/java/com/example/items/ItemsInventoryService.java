package com.example.items;

import com.example.users.User;
import com.example.users.inventory.Inventory;
import com.example.users.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ItemsInventoryService {
    private final InventoryService inventoryService;
    private final ItemService itemService;

    @Autowired
    public ItemsInventoryService(InventoryService inventoryService, ItemService itemService) {
        this.inventoryService = inventoryService;
        this.itemService = itemService;
    }


    public Item handleOnNewUserItem(User user, Item item){
        item.setUser(user);

        Item createdItem = this.itemService.create(item);
        Inventory userInventory = this.inventoryService.getUserInventory(user.getId());

        userInventory.addItem(createdItem);

        this.inventoryService.update(userInventory);
        return createdItem;
    }
}


