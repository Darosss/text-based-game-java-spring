package com.example.items;

import com.example.auth.SecuredRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PostMapping("items/debug/createItem/{level}/{type}")
    public Item generateItemWithLevelAndType(
            @PathVariable int level,
            @PathVariable ItemTypeEnum type
    ){
        return service.create(ItemUtils.generateRandomItemWithoutBaseStats(
                "Item custom level", level, type
        ));
    }

    @DeleteMapping("items/debug/delete-all")
    public void deleteAllItems() {
        service.removeAllItems();
    }
}
