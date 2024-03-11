package com.example.merchants;

import com.example.items.Item;
import com.example.items.ItemTypeEnum;
import com.example.items.ItemUtils;
import com.example.users.User;
import com.example.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class MerchantsUtils {
    private MerchantsUtils() {};

    public static List<Item> generateMerchantsItems(User user, int characterLevel) {
        List<Item> generatedItems = new ArrayList<>();
        for (ItemTypeEnum itemType : ItemTypeEnum.values()) {
            ItemTypeEnum.MerchantOptions options = itemType.getMerchantOptions();
            int generateCount = RandomUtils.getRandomValueWithinRange(options.minItems(), options.maxItems());
            for (int i = 0; i < generateCount; i++){
                int itemLevel = RandomUtils.getRandomValueWithinRange(
                        Math.max(1, characterLevel - 5), characterLevel + 12
                );
                generatedItems.add(ItemUtils.generateRandomItemWithoutBaseStats(
                        user,itemType + " from merchant | itemLevel: "+itemLevel, itemLevel, itemType)
                );

            }

        }

        return generatedItems;
    }


}
