package com.example.characters;

import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.items.*;
import com.example.statistics.BaseStatisticObject;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.users.User;
import com.example.users.UserService;
import com.example.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;

@RestController
public class CharactersController {
    private CharacterService service;
    private ItemService itemService;
    private UserService userService;

    @Autowired
    public CharactersController(CharacterService characterService, ItemService itemService,
                                UserService userService) {
        this.service = characterService;
        this.itemService = itemService;
        this.userService = userService;
    }
    @GetMapping("/characters")

    public List<Character> findAll() {
              return this.service.findAll();
    }

    @PostMapping("/characters/{userId}")
    public Character create(@PathVariable String userId){
        //userID later from as loggedin

        Optional<User> user = this.userService.findOneById(userId);
        return user.map(value -> service.create(value)).orElse(null);
    }

    @PostMapping("/characters-debug/{userId}")
    public Character createDebug(@PathVariable String userId){
        //userID later from as logged in
        Optional<User> foundUser = this.userService.findOneById(userId);
        if(foundUser.isPresent()) {
            User userInst = foundUser.get();
            Character createdChar = service.createDebugCharacter(
                    userInst,
                    RandomUtils.getRandomValueWithinRange(100, 2000),
                    RandomUtils.getRandomValueWithinRange(100, 20000),
                    RandomUtils.getRandomValueWithinRange(1, 55),
                    RandomUtils.getRandomValueWithinRange(1, 6),
                    RandomUtils.getRandomValueWithinRange(1, 32),
                    RandomUtils.getRandomValueWithinRange(2333, 2555));

            userInst.addCharacter(createdChar);
            this.userService.update(userInst);
            return createdChar;
        }
        return null;
    }
    @GetMapping("/characters/statistics/{name}/effective-value")
        public int getEffectiveValueByStatName(@PathVariable BaseStatisticsNamesEnum name) {
            Character foundChar = service.findOne();

        return foundChar.getStatistics().get(name).getEffectiveValue();

    }

    @GetMapping("/characters/statistics/{name}")
    public BaseStatisticObject getCharacterStats(@PathVariable BaseStatisticsNamesEnum name) {
        Character foundChar = service.findOne();
        if(foundChar == null) return null;

        return foundChar.getStatistics().get(name);


    }
    @PostMapping("/characters/equip/{characterId}/{itemId}/{slot}")
    public boolean equipCharacterItem(
            @PathVariable String characterId,
            @PathVariable String itemId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) {
        Optional<Item> itemToEquip = this.itemService.findOne(itemId);
        return itemToEquip.filter(item -> this.service.equipItem(characterId, slot, item)).isPresent();
    }

    @PostMapping("/characters/un-equip/{characterId}/{slot}")
    public Item unEquipCharacterItem(
            @PathVariable String characterId,
            @PathVariable CharacterEquipmentFieldsEnum slot,
            @PathVariable ItemTypeEnum itemType
    ) {

        return this.service.unequipItem(characterId, slot);
    }

}
