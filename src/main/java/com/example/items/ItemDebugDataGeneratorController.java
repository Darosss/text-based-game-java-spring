package com.example.items;

import com.example.auth.AuthenticationFacade;
import com.example.auth.LoggedUserUtils;
import com.example.auth.SecuredRestController;
import com.example.response.CustomResponse;
import com.example.users.User;
import com.example.users.UserService;
import com.example.users.inventory.Inventory;
import com.example.users.inventory.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public CustomResponse<List<Item>> getItems(){
        return new CustomResponse<>(HttpStatus.OK, service.findAll());
    }

    @PostMapping("items/debug/create-with-random-data/{countOfItems}/")
    public CustomResponse<List<Item>> generateItemWithRandomData(@PathVariable int countOfItems) throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);
        Inventory inventory = this.inventoryService.getUserInventory(loggedUser.getId());
        List<Item> items = this.service.create(ItemUtils.generateRandomItems(countOfItems));
        for (Item item : items) {
            inventory.addItem(item);
        }
        this.inventoryService.update(inventory);

        return new CustomResponse<>(HttpStatus.OK, items);
    }

    @PostMapping("items/debug/createItem/{level}/{type}")
    public CustomResponse<Item> generateItemWithLevelAndType(
            @PathVariable int level,
            @PathVariable ItemTypeEnum type
    ) throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);
        Inventory inventory = this.inventoryService.getUserInventory(loggedUser.getId());
        Item item = service.create(ItemUtils.generateRandomItemWithoutBaseStats(
                "Item custom level", level, type));
        inventory.addItem(item);
        this.inventoryService.update(inventory);

        return new CustomResponse<>(HttpStatus.OK, item);
    }

    @DeleteMapping("items/debug/delete-all")
    public void deleteAllItems() {
        this.service.removeAllItems();
    }
}
