package com.example.characters;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.items.*;
import com.example.statistics.BaseStatisticObject;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.users.User;
import com.example.users.UserService;
import com.example.utils.RandomUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController("characters")
public class CharactersController implements SecuredRestController {
    private CharacterService service;
    private ItemService itemService;
    private final AuthenticationFacade authenticationFacade;
    private UserService userService;

    @Autowired
    public CharactersController(CharacterService characterService, ItemService itemService,
                                UserService userService, AuthenticationFacade authenticationFacade) {
        this.service = characterService;
        this.itemService = itemService;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    //Later for admins
    @GetMapping("/")
    public List<Character> findAll() {
              return this.service.findAll();
    }

    @GetMapping("/your-characters")
    public List<Character> findYourCharacters() throws Exception {
        return this.service.findUserCharacters(this.authenticationFacade.getJwtTokenPayload().id());
    }

    @PostMapping("/create")
    public Character create() throws Exception {

        Optional<User> user = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());

        if(user.isPresent()) {
            User userInst = user.get();
            Character createdChar = service.create(user.get());
            userInst.addCharacter(createdChar);
            this.userService.update(userInst);
        }
        return null;
    }
    @PostMapping("/debug/create-empty-char/{userId}")
    public Character create(@PathVariable String userId){
        Optional<User> user = this.userService.findOneById(userId);
        return user.map(value -> service.create(value)).orElse(null);
    }
    @PostMapping("/debug/create-with-random-stats")
    public Character createDebug() throws Exception {
        //userID later from as logged in
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
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
    @GetMapping("/statistics/{name}/effective-value")
        public int getEffectiveValueByStatName(@PathVariable BaseStatisticsNamesEnum name) {
            Character foundChar = service.findOne();

        return foundChar.getStatistics().get(name).getEffectiveValue();

    }

    @GetMapping("/statistics/{name}")
    public BaseStatisticObject getCharacterStats(@PathVariable BaseStatisticsNamesEnum name) {
        Character foundChar = service.findOne();
        if(foundChar == null) return null;

        return foundChar.getStatistics().get(name);


    }
    @PostMapping("/equip/{characterId}/{itemId}/{slot}")
    public boolean equipCharacterItem(
            @PathVariable String characterId,
            @PathVariable String itemId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<Item> itemToEquip = this.itemService.findOne(itemId);
        Optional<Character> foundCharacter = this.service.findById(characterId);
        if(foundCharacter.isPresent() &&
                foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId))
        )
        {
            return itemToEquip.filter(item -> this.service.equipItem(characterId, slot, item)).isPresent();
        }

        return false;
    }

    @PostMapping("/un-equip/{characterId}/{slot}")
    public Item unEquipCharacterItem(
            @PathVariable String characterId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<Character> foundCharacter = this.service.findById(characterId);
            if(foundCharacter.isPresent() &&
                    foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
            {
            return this.service.unequipItem(characterId, slot);
        }
        return null;
    }


    @PatchMapping("/train-statistic/{statisticName}/{addValue}")
    public boolean TrainStatistic(@PathVariable BaseStatisticsNamesEnum statisticName,
                                  @PathVariable int addValue
                                  ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<Character> foundCharacter = this.service.findOneByUserId(loggedUserId);
        if(foundCharacter.isPresent() &&
                foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
        {
            Character characterInst = foundCharacter.get();
            characterInst.getStatistics().get(statisticName).addToValue(addValue);
            this.service.update(characterInst);
            return true;
        }
        return false;
    }

    @PatchMapping("/debug/subtract-statistic/{statisticName}/{subtractValue}")
    public boolean DebugSubtractTrainStatistic(@PathVariable BaseStatisticsNamesEnum statisticName,
                                  @PathVariable int subtractValue
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<Character> foundCharacter = this.service.findOneByUserId(loggedUserId);
        if(foundCharacter.isPresent() &&
                foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
        {
            Character characterInst = foundCharacter.get();
            characterInst.getStatistics().get(statisticName).subtractFromValue(subtractValue);
            this.service.update(characterInst);
            return true;
        }
        return false;
    }
}
