package com.example.characters.inventory;

import com.example.items.Item;
import com.example.users.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "characters_inventories")
public class Inventory {
    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private List<Item> items;
    private int maxItems;
    private float maxWeight;
    private float currentWeight;

    public Inventory(){}
    public Inventory(int maxWeight) {
        this.items = new ArrayList<>();
        this.maxItems = 100;
        this.maxWeight = maxWeight;
        this.currentWeight = 0;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public boolean addItem(Item item) {
        if (items.size() >= maxItems || currentWeight + item.getWeight() > maxWeight) {
            return false;
        }

        items.add(item);
        currentWeight += item.getWeight();
        return true;
    }

    public boolean removeItem(Item item) {
        if (items.remove(item)) {
            currentWeight -= item.getWeight();
            return true; // Item removed successfully
        }
        return false; // Item not found in the inventory
    }


}
