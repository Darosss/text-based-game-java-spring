package com.example.utils;

import com.example.characters.Character;
import com.example.characters.equipment.CharacterEquipment;
import com.example.items.Item;
import com.example.merchants.Merchant;
import com.example.users.User;
import com.example.users.inventory.Inventory;
import dev.morphia.query.filters.Filters;
import dev.morphia.transactions.MorphiaSession;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class TransactionsUtils {
    private TransactionsUtils() {}

    public static Inventory fetchUserInventory(MorphiaSession session, ObjectId userId) {
        return session.find(Inventory.class)
                .filter(Filters.eq("user", userId))
                .first();
    }
    public static Inventory fetchUserInventory(MorphiaSession session, String userId) {
        return fetchUserInventory(session, new ObjectId(userId));
    }
    public static <T extends Character>  T fetchCharacter(MorphiaSession session, ObjectId characterId, ObjectId userId, Class<T> characterClass) {
        return session.find(characterClass)
                .filter(Filters.eq("user", userId) ,
                        Filters.eq("id", characterId)
                )
                .first();
    }
    public static Merchant fetchUserMerchant(MorphiaSession session, ObjectId userId) {
        return session.find(Merchant.class)
                .filter(Filters.eq("user", userId))
                .first();
    }
    public static Merchant fetchUserMerchant(MorphiaSession session, String userId) {
        return fetchUserMerchant(session, new ObjectId(userId));
    }

    public static User fetchUser(MorphiaSession session, ObjectId userId) {
        return session.find(User.class)
                .filter(Filters.eq("id", userId))
                .first();
    }
    public static User fetchUser(MorphiaSession session, String userId) {
        return fetchUser(session, new ObjectId(userId));
    }

    public static List<Item> handleCreatingNewItems(MorphiaSession session, List<Item> items){
        List<Item> itemsDb = new ArrayList<>();
        for (Item item : items) {
            Item createdItem = session.save(item);
            itemsDb.add(createdItem);
        }
        return itemsDb;
    }

    public static CharacterEquipment createNewEquipment(MorphiaSession session){
        return session.save(new CharacterEquipment());
    }
}
