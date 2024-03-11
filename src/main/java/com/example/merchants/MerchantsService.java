package com.example.merchants;

import com.example.items.Item;
import com.example.users.User;
import com.example.users.inventory.Inventory;
import com.example.utils.TransactionsUtils;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import dev.morphia.transactions.MorphiaSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MerchantsService {

    private final Datastore datastore;

    @Autowired
    public MerchantsService(Datastore datastore) {
        this.datastore = datastore;
    }

    public record MerchantActionReturn(boolean success, Optional<Item> item, String message){}

    public Optional<Merchant> findMerchantByUserId(ObjectId userId){
        return Optional.ofNullable(datastore.find(Merchant.class).filter(
                Filters.eq("user", userId)
        ).first());
    }
    public Optional<Merchant> findMerchantByUserId(String userId){
        return findMerchantByUserId(new ObjectId(userId));
    }
    public Merchant create(User user, List<Item> items) throws Exception {
        try(MorphiaSession session = datastore.startSession()) {
            session.startTransaction();

            Merchant createdMerchant = session.save(
                    new Merchant(user, TransactionsUtils.handleCreatingNewItems(session, items)));
            session.commitTransaction();

            return createdMerchant;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    };
    public MerchantActionReturn buyItemFromMerchant(String userId, String itemId) throws Exception {
        try(MorphiaSession session = datastore.startSession()) {
            session.startTransaction();


            Inventory userInventory = TransactionsUtils.fetchUserInventory(session, userId);
            Merchant userMerchant = TransactionsUtils.fetchUserMerchant(session, userId);
            User user = TransactionsUtils.fetchUser(session, userId);
            if(userInventory == null || userMerchant == null || user == null)
                return new MerchantActionReturn(false, Optional.empty(),
                        "Cannot find inventory or user or merchant data. Contact administration");


            Merchant.MerchantTransaction boughItemData = userMerchant.buyItemByItemId(itemId);
            if(boughItemData.item().isEmpty())
                return new MerchantActionReturn(false, Optional.empty(), "This item does not exist in merchant commodity");

            if(boughItemData.cost() > user.getGold())
                return new MerchantActionReturn(false, Optional.empty(), "You do not have enough gold");

            userInventory.addItem(boughItemData.item().get());
            user.decreaseGold(boughItemData.cost());
            session.save(user);
            session.save(userInventory);
            session.save(userMerchant);

            session.commitTransaction();

            return new MerchantActionReturn(true, boughItemData.item(), "Successfully bought item");
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public MerchantActionReturn sellItemToMerchant(String userId, String itemId) throws Exception {
        try(MorphiaSession session = datastore.startSession()) {
            session.startTransaction();

            Inventory userInventory = TransactionsUtils.fetchUserInventory(session, userId);
            Merchant userMerchant = TransactionsUtils.fetchUserMerchant(session, userId);
            User user = TransactionsUtils.fetchUser(session, userId);

            if(userInventory == null || userMerchant == null || user == null)
                return new MerchantActionReturn(false, Optional.empty(),
                        "Cannot find inventory or user or merchant data. Contact administration");


            Optional<Item> item = userInventory.getItemById(itemId);
            if(item.isEmpty()) return new MerchantActionReturn(false, Optional.empty(),
                    "This item does not exist in your inventory");


            Merchant.MerchantTransaction sellItemData = userMerchant.sellItem(item.get());

            userInventory.removeItemById(itemId);
            user.increaseGold(sellItemData.cost());
            session.save(user);
            session.save(userInventory);
            session.save(userMerchant);

            session.commitTransaction();

            return new MerchantActionReturn(true, sellItemData.item(), "Successfully sold item");
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Merchant update(Merchant merchant) {
        return datastore.save(merchant);
    }

    public Merchant getOrCreateMerchant(User user, int mainCharacterLevel) throws Exception {
        Optional<Merchant> foundMerchant = this.findMerchantByUserId(user.getId());

        if(foundMerchant.isEmpty()) {
            List<Item> items = MerchantsUtils.generateMerchantsItems(user, mainCharacterLevel);
            return this.create(user, items);
        }

        Merchant merchant = foundMerchant.get();
        if(merchant.isCommodityExpired()) {
            List<Item> newItems = MerchantsUtils.generateMerchantsItems(user, mainCharacterLevel);

            this.handleRefreshCommodity(merchant, newItems);
        }

        return merchant;
    }

    private void handleRefreshCommodity (Merchant merchant, List<Item> newItems) throws Exception {
        try (MorphiaSession session = datastore.startSession()) {
            session.startTransaction();


            for(String itemId: merchant.getItems().keySet()){
                session.find(Item.class)
                        .filter(Filters.eq("id", new ObjectId(itemId)))
                        .delete();
            }
            merchant.setNewCommodity(TransactionsUtils.handleCreatingNewItems(session, newItems));
            session.save(merchant);
            session.commitTransaction();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
