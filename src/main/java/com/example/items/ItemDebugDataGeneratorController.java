package com.example.items;

import com.example.auth.SecuredRestController;
import com.example.items.statistics.ItemAdditionalStatisticsMap;
import com.example.items.statistics.ItemBaseStatisticsMap;
import com.example.items.statistics.ItemStatisticsObject;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//CONTROLLER FOR DEBUGGING
//EASILY ADD RANDOM ITEMS
//ALL METHODS PUBLIC FOR EASY DEBUG ACCESS
@RestController
public class ItemDebugDataGeneratorController implements SecuredRestController {
    private ItemService service;

    @Autowired
    public ItemDebugDataGeneratorController(ItemService service) {
        this.service = service;
    }

    @GetMapping("items/")
    public List<Item> getItems(){
        return service.findAll();
    }

    @PostMapping("items/debug/create-with-random-data/{countOfItems}/")
    public List<Item> generateItemWithRandomData(@PathVariable int countOfItems){
        return service.create(ItemUtils.generateRandomItems(countOfItems));
    }

    @DeleteMapping("items/debug/delete-all")
    public void deleteAllItems() {
        service.removeAllItems();
    }
}
