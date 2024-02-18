package com.example.items;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.users.User;
import com.example.users.UserService;
import com.example.users.inventory.Inventory;
import com.example.users.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

//CONTROLLER FOR DEBUGGING
//EASILY ADD RANDOM ITEMS
//ALL METHODS PUBLIC FOR EASY DEBUG ACCESS
@RestController
public class ItemDebugDataGeneratorController implements SecuredRestController {
    private final ItemService service;
    private final UserService userService;
    private final InventoryService inventoryService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ItemDebugDataGeneratorController(ItemService service, UserService userService, InventoryService inventoryService, AuthenticationFacade authenticationFacade) {
        this.service = service;
        this.userService = userService;
        this.inventoryService = inventoryService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping("items/")
    public List<Item> getItems(){
        return service.findAll();
    }

    @PostMapping("items/debug/create-with-random-data/{countOfItems}/")
    public List<Item> generateItemWithRandomData(@PathVariable int countOfItems) throws Exception {
        String userId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<User> user = this.userService.findOneById(userId);
        Inventory inventory = this.inventoryService.getUserInventory(userId);
        if(user.isPresent()) {
            List<Item> items = this.service.create(ItemUtils.generateRandomItems(countOfItems));
            for (Item item : items) {
                inventory.addItem(item);
            }
            this.inventoryService.update(inventory);

            return items;
        }
        return null;
    }

    @PostMapping("items/debug/createItem/{level}/{type}")
    public Item generateItemWithLevelAndType(
            @PathVariable int level,
            @PathVariable ItemTypeEnum type
    ) throws Exception {
        String userId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<User> user = this.userService.findOneById(userId);
        Inventory inventory = this.inventoryService.getUserInventory(userId);
        if(user.isPresent()) {
            Item item = service.create(ItemUtils.generateRandomItemWithoutBaseStats(
                    "Item custom level", level, type));
            inventory.addItem(item);
            inventoryService.update(inventory);

            return item;
        }

        return null;
    }

    @DeleteMapping("items/debug/delete-all")
    public void deleteAllItems() {
        service.removeAllItems();
    }
}
