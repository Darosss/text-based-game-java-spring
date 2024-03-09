package com.example.users.inventory;

import com.example.items.Item;
import com.example.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Entity("users_inventories")
public class Inventory {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;

    @JsonIgnoreProperties("inventory")
    @Reference(idOnly = true, lazy = true)
    private User user;

    @JsonIgnoreProperties("user")
    @Reference
    private Map<String, Item> items;
    private int maxItems=100;
    private float maxWeight=100;
    private float currentWeight = 0;

    public Inventory(){}
    public Inventory(int maxWeight) {
        this.items = new HashMap<>();
        this.maxItems = 1000;
        this.maxWeight = maxWeight;
        this.currentWeight = 0;
    }

    public Map<String, Item> getItems() {
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
        if(this.items == null) this.items = new HashMap<>();
        if (this.items.size() >= this.maxItems || this.currentWeight + item.getWeight() > this.maxWeight) {
            return false;
        }
        items.put(item.getId().toString(), item);
        currentWeight += item.getWeight();
        return true;
    }


    public Optional<Item> removeItemById(String id) {
        if (this.items.containsKey(id)) {
            Item item = this.items.remove(id);
            currentWeight -= item.getWeight();
            return Optional.of(item);
        }
        return Optional.empty();
    }


    public Optional<Item> removeItemById(ObjectId id) {
        String idToFind = id.toString();
        return this.removeItemById(idToFind);
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

    public void setItems(Map<String, Item> items) {
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

    public Optional<Item> getItemById(String itemId) {
        return Optional.ofNullable(this.items.get(itemId));
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
