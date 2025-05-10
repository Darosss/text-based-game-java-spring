package com.textbasedgame.merchants;

import com.textbasedgame.items.Item;
import com.textbasedgame.settings.Settings;
import com.textbasedgame.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.*;

@Entity("merchants")
public class Merchant {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;

    @Reference(idOnly = true, lazy = true)
    @JsonIgnore
    private User user;

    @JsonIgnoreProperties("user")
    @Reference(idOnly = true)
    private final Map<String, Item> items = new HashMap<>();

    private final Map<String, Integer> itemsCost = new HashMap<>();

    private LocalDateTime commodityRefreshAt;

    Merchant() {}
    public record MerchantTransaction(Optional<Item> item, int cost){}
    public Merchant(User user, List<Item> itemsList) {
        this.user = user;
        this.setNewCommodity(itemsList);
    }
    public ObjectId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public LocalDateTime getCommodityRefreshAt() {
        return commodityRefreshAt;
    }

    public MerchantTransaction buyItemByItemId(String itemId) {
        if(!this.items.containsKey(itemId)) return new MerchantTransaction(Optional.empty(), 0);

        Item boughtItem = this.items.remove(itemId);

        return new MerchantTransaction(Optional.ofNullable(boughtItem),
                this.itemsCost.get(Objects.requireNonNull(boughtItem).getId().toString()));
    }

    public MerchantTransaction sellItem(Item item) {
        String itemIdString = item.getId().toString();
        this.items.put(itemIdString, item);
        this.itemsCost.put(itemIdString, item.getValue());

        return new MerchantTransaction(Optional.of(item), this.itemsCost.get(item.getId().toString()));
    }


    public Map<String, Integer> getItemsCost() {
        return itemsCost;
    }

    public boolean isCommodityExpired(){
        return this.commodityRefreshAt.isBefore(LocalDateTime.now());
    }

   public void setNewCommodity(List<Item> newItems) {
       this.items.clear();
       for(Item item: newItems) {
           String itemIdString = item.getId().toString();
           this.items.put(itemIdString ,item);
           this.itemsCost.put(itemIdString, item.getValue() * Settings.MERCHANT_VALUE_BUY_COST_COMMODITY_MULTIPLIER);
       }
       this.commodityRefreshAt = LocalDateTime.now().plusHours(Settings.MERCHANT_COMMODITY_REFRESH_HOURS);
   }

}
