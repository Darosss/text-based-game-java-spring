package com.example.lootlogic;

import com.example.items.Item;
import com.example.items.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//NOTE: THIS SERVICE IS FOR DEBUG - THOSE METHODS WILL BE USED WHEN:
//- user defeat enemy
//- open something etc.
@Service
public class ItemGeneratorService {

    private final ItemService itemService;

    @Autowired
    public ItemGeneratorService(ItemService itemService) {
        this.itemService = itemService;
    }

//    public Item generateAndSaveRandomItem() {
//        Item randomItem = generateRandomItem();
//        itemService.create(randomItem);
//        return randomItem;
//    }
//
//    private Item generateRandomItem() {
//
////        return new Item(/* generated properties */);
//    }


}