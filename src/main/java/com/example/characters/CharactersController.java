package com.example.characters;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.items.*;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticObject;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.users.User;
import com.example.users.UserService;
import com.example.utils.RandomUtils;
import org.apache.coyote.BadRequestException;
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

    private final CharacterInventoryService characterInventoryService;

    @Autowired
    public CharactersController(CharacterService characterService, ItemService itemService,
                                UserService userService,
                                AuthenticationFacade authenticationFacade,
                                CharacterInventoryService characterInventoryService) {
        this.service = characterService;
        this.itemService = itemService;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.characterInventoryService = characterInventoryService;
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
    @GetMapping("/your-main-character")
    public Optional<Character> findYourMainCharacter() throws Exception {
        return this.service.findOneMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());
    }

    @PostMapping("/create")
    public Character create() throws Exception {

        Optional<User> user = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());

        if(user.isPresent()) {
            User userInst = user.get();
            if(this.service.findOneMainCharacterByUserId(userInst.getId().toString()).isPresent()){
                throw new BadRequestException("User already have main character");
            }

            Character createdChar = service.create(user.get(), true);
            userInst.addCharacter(createdChar);
            this.userService.update(userInst);
        }
        return null;
    }
    @PostMapping("/debug/create-empty-char/{userId}/{asMainCharacter}")
    public Character create(@PathVariable String userId,
                            @PathVariable boolean asMainCharacter
    ){
        Optional<User> user = this.userService.findOneById(userId);
        return user.map(value -> service.create(value, true)).orElse(null);
    }
    @PostMapping("/debug/create-with-random-stats/{asMainCharacter}")
    public Character createDebug(
            @PathVariable boolean asMainCharacter
    ) throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isPresent()) {
            User userInst = foundUser.get();
            Character createdChar = service.createDebugCharacter(
                    userInst,
                    RandomUtils.getRandomValueWithinRange(1, 55),
                    RandomUtils.getRandomValueWithinRange(4, 909),
                    asMainCharacter,
                    Map.of(
                            BaseStatisticsNamesEnum.STRENGTH, RandomUtils.getRandomValueWithinRange(1, 55),
                            BaseStatisticsNamesEnum.DEXTERITY, RandomUtils.getRandomValueWithinRange(1, 55),
                            BaseStatisticsNamesEnum.CHARISMA, RandomUtils.getRandomValueWithinRange(1, 55),
                            BaseStatisticsNamesEnum.CONSTITUTION, RandomUtils.getRandomValueWithinRange(1, 55),
                            BaseStatisticsNamesEnum.INTELLIGENCE, RandomUtils.getRandomValueWithinRange(1, 55),
                            BaseStatisticsNamesEnum.LUCK, RandomUtils.getRandomValueWithinRange(1, 55)
                    ),
                    Map.of(
                            AdditionalStatisticsNamesEnum.INITIATIVE, 10,
                            AdditionalStatisticsNamesEnum.MIN_DAMAGE, 1,
                            AdditionalStatisticsNamesEnum.MAX_DAMAGE, 2,
                            AdditionalStatisticsNamesEnum.MAX_HEALTH, 350
                            )
                    );

            userInst.addCharacter(createdChar);
            this.userService.update(userInst);
            return createdChar;
        }
        return null;
    }
    @GetMapping("/statistics/{name}/effective-value")
        public int getEffectiveValueByStatName(@PathVariable BaseStatisticsNamesEnum name) {
            Character foundChar = service.findOne();

        return foundChar.getStats().getStatistics().get(name).getEffectiveValue();

    }
    @GetMapping("/statistics/{name}")
    public BaseStatisticObject getCharacterStats(@PathVariable BaseStatisticsNamesEnum name) {
        Character foundChar = service.findOne();
        if(foundChar == null) return null;

        return foundChar.getStats().getStatistics().get(name);


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
        if(     itemToEquip.isPresent() &&
                foundCharacter.isPresent() && foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId))
        ){
            return this.characterInventoryService.equipItem(new ObjectId(loggedUserId), itemToEquip.get(), slot);
        }

        return false;
    }

    @PostMapping("/un-equip/{characterId}/{slot}")
    public boolean unEquipCharacterItem(
            @PathVariable String characterId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<Character> foundCharacter = this.service.findById(characterId);
            if(foundCharacter.isPresent() &&
                    foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
            {
                return this.characterInventoryService.unEquipItem(new ObjectId(loggedUserId), slot);
            }
        return false;
    }


    @PatchMapping("/train-statistic/{statisticName}/{addValue}")
    public boolean trainStatistic(@PathVariable BaseStatisticsNamesEnum statisticName,
                                  @PathVariable int addValue
                                  ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<Character> foundCharacter = this.service.findOneByUserId(loggedUserId);
        if(foundCharacter.isPresent() &&
                foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
        {
            Character characterInst = foundCharacter.get();
            characterInst.getStats().getStatistics().get(statisticName).increaseValue(addValue);
            this.service.update(characterInst);
            return true;
        }
        return false;
    }

    @PatchMapping("/debug/subtract-statistic/{statisticName}/{subtractValue}")
    public boolean debugSubtractTrainStatistic(@PathVariable BaseStatisticsNamesEnum statisticName,
                                  @PathVariable int subtractValue
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<Character> foundCharacter = this.service.findOneByUserId(loggedUserId);
        if(foundCharacter.isPresent() &&
                foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
        {
            Character characterInst = foundCharacter.get();
            characterInst.getStats().getStatistics().get(statisticName).decreaseValue(subtractValue);
            this.service.update(characterInst);
            return true;
        }
        return false;
    }

    @DeleteMapping("debug/delete-all")
    public void deleteAllItems() {
        service.removeAllCharactersAndEquipments();
    }
}
