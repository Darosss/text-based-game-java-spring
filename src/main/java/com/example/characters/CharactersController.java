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
        Optional<User> user = this.userService.findOneById(userId);
        return user.map(value -> service.createDebugCharacter(
                value,
                RandomUtils.getRandomValueWithinRange(100,2000),
                RandomUtils.getRandomValueWithinRange(100,20000),
                RandomUtils.getRandomValueWithinRange(1,55),
                RandomUtils.getRandomValueWithinRange(1,6),
                RandomUtils.getRandomValueWithinRange(1,32),
                RandomUtils.getRandomValueWithinRange(2333,2555))).orElse(null);

    }
    @GetMapping("/characters/statistics/{name}/effective-value")
        public int getEffectiveValueByStatName(@PathVariable BaseStatisticsNamesEnum name) {
            Optional<Character> foundChar = service.findOne();

        return foundChar.map(character -> character.getStatistics().
                get(name).getEffectiveValue()).orElse(0);

    }

    @GetMapping("/characters/statistics/{name}")
    public Optional<BaseStatisticObject> getCharacterStats(@PathVariable BaseStatisticsNamesEnum name) {
        Optional<Character> foundChar = service.findOne();

        return foundChar.map(character -> character.getStatistics().get(name));


    }
    @PostMapping("/characters/equip/{itemId}/{slot}")
    public boolean equipCharacterItem(
            @PathVariable String itemId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) {
        Optional<Item> itemToEquip = this.itemService.findOne(itemId);
        return itemToEquip.filter(item -> this.service.equipItem("65aec1d2dc2f3d1083700038",slot, item)).isPresent();
    }

    @PostMapping("/characters/un-equip/{slot}")
    public Item unEquipCharacterItem(
            @PathVariable CharacterEquipmentFieldsEnum slot,
            @PathVariable ItemTypeEnum itemType
    ) {

        return this.service.unequipItem("65aec1d2dc2f3d1083700038", slot);
    }

}
