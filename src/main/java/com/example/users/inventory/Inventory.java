package com.example.users.inventory;

import com.example.items.Item;
import com.example.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Entity("users_inventories")
public class Inventory {
    @Id
    private ObjectId id;

    @Reference(idOnly = true, lazy = true, ignoreMissing = true)
    private User user;

    @Reference
    private List<Item> items;
    private int maxItems=100;
    private float maxWeight=100;
    private float currentWeight = 0;


    public Inventory(){}
    public Inventory(int maxWeight) {
        this.items = new ArrayList<>();
        this.maxItems = 1000;
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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public void setMaxWeight(float maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", user=" + user +
                ", items=" + items +
                ", maxItems=" + maxItems +
                ", maxWeight=" + maxWeight +
                ", currentWeight=" + currentWeight +
                '}';
    }
}
