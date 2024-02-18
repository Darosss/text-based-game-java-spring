package com.example.characters;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.characters.equipment.Equipment.EquipItemResult;
import com.example.characters.equipment.Equipment.UnEquipItemResult;
import com.example.items.*;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticObject;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.users.User;
import com.example.users.UserService;
import com.example.users.inventory.Inventory;
import com.example.users.inventory.InventoryService;
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
    private InventoryService inventoryService;

    private final CharacterInventoryService characterInventoryService;

    @Autowired
    public CharactersController(CharacterService characterService, ItemService itemService,
                                UserService userService,
                                AuthenticationFacade authenticationFacade,
                                CharacterInventoryService characterInventoryService,
                                InventoryService inventoryService) {
        this.service = characterService;
        this.itemService = itemService;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.inventoryService = inventoryService;
        this.characterInventoryService = characterInventoryService;
    }

    @GetMapping("/your-mercenaries")
    public List<MercenaryCharacter> findYourMercenaries() throws Exception {
        return this.service.findUserMercenaries(this.authenticationFacade.getJwtTokenPayload().id());
    }
    @GetMapping("/your-main-character")
    public Optional<MainCharacter> findYourMainCharacter() throws Exception {
        return this.service.findMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());
    }
    @GetMapping("characters/{characterId}")
    public Optional<Character> findYourCharacterById(@PathVariable String characterId) {
        return this.service.findById(characterId);
    }

    @GetMapping("debug/all")
    public List<Character> findAll()  {
        return this.service.findAll();
    }

    @PostMapping("/create")
    public MainCharacter createMainCharacter() throws Exception {

        Optional<User> user = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());

        if(user.isPresent()) {
            User userInst = user.get();
            if(this.service.findMainCharacterByUserId(userInst.getId().toString()).isPresent()){
                throw new BadRequestException("User already have main character");
            }

            MainCharacter createdChar = service.createMainCharacter(user.get(), userInst.getUsername());
            userInst.addCharacter(createdChar);
            this.userService.update(userInst);
        }
        return null;
    }

    // For debug ignore ;p
    @PostMapping("/debug/create-empty-char/{userId}/{name}/{asMainCharacter}")
    public Character create(@PathVariable String userId,
                            @PathVariable String name,
                            @PathVariable boolean asMainCharacter
    ){
        Optional<User> user = this.userService.findOneById(userId);
        if(asMainCharacter) {
            return user.map(value -> service.createMainCharacter(value, name)).orElse(null);
        }else {
            return user.map(value -> service.createMercenaryCharacter(value, name)).orElse(null);
        }
    }


    @PostMapping("/debug/create-mercenary-character-with-random-stats")
        public MercenaryCharacter createDebugMercenary(
                ) throws Exception {
            Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
            if(foundUser.isPresent()) {
                User userInst = foundUser.get();
                MercenaryCharacter createdChar = service.createMercenaryCharacter(
                        userInst, "MERCENARY"
                );
                userInst.addCharacter(createdChar);
                this.userService.update(userInst);
                return createdChar;
            }
            return null;
        }

    @PostMapping("/debug/create-main-character-with-random-stats")
    public Character createDebug(
    ) throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isPresent()) {
            User userInst = foundUser.get();
            MainCharacter createdChar = service.createDebugCharacter(
                    userInst,
                    RandomUtils.getRandomValueWithinRange(1, 55),
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

    @PostMapping("/use-consumable/{itemId}")
    public boolean useConsumable(
            @PathVariable String itemId
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<MainCharacter> mainCharacter = this.service.findMainCharacterByUserId(loggedUserId);
        Optional<ItemConsumable> itemToUse = this.itemService.findOne(itemId, ItemConsumable.class);

        Inventory inventory = this.inventoryService.getUserInventory(loggedUserId);

        if(mainCharacter.isEmpty() || itemToUse.isEmpty()) return false;
        return this.characterInventoryService.useConsumableItem(inventory, mainCharacter.get(),  itemToUse.get());
    };
    @PostMapping("/equip/{characterId}/{itemId}/{slot}")
    public EquipItemResult equipCharacterItem(
            @PathVariable String characterId,
            @PathVariable String itemId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<ItemWearable> itemToEquip = this.itemService.findOne(itemId, ItemWearable.class);
        if(itemToEquip.isPresent()){
            return this.characterInventoryService.equipItem(new ObjectId(loggedUserId), new ObjectId(characterId), itemToEquip.get(), slot);
        }

        return new EquipItemResult(false, "Something went wrong. Try again later.");
    }

    @PostMapping("/un-equip/{characterId}/{slot}")
    public UnEquipItemResult unEquipCharacterItem(
            @PathVariable String characterId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();

        return this.characterInventoryService.unEquipItem(new ObjectId(loggedUserId),  new ObjectId(characterId), slot);
    }


    @PostMapping("/equip-mercenary/{characterId}/{itemId}")
    public EquipItemResult equipMercenary(
            @PathVariable String characterId,
            @PathVariable String itemId
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<ItemMercenary> itemToEquip = this.itemService.findOne(itemId, ItemMercenary.class);
        if(itemToEquip.isPresent()){
            return this.characterInventoryService.useMercenaryItemOnMercenaryCharacter(
                    new ObjectId(loggedUserId), new ObjectId(characterId), itemToEquip.get());
        }
        return new EquipItemResult(false, "Item not found");
    }
    @PostMapping("/un-equip-mercenary/{characterId}")
    public UnEquipItemResult unEquipMercenary(
            @PathVariable String characterId
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        return this.characterInventoryService.unEquipMercenaryItemFromMercenaryCharacter(
                new ObjectId(loggedUserId), new ObjectId(characterId));
    }
    @PatchMapping("/train-statistic/{statisticName}/{addValue}")
    public boolean trainStatistic(@PathVariable BaseStatisticsNamesEnum statisticName,
                                  @PathVariable int addValue
                                  ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<MainCharacter> foundCharacter = this.service.findMainCharacterByUserId(loggedUserId);
        if(foundCharacter.isPresent() &&
                foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
        {
            MainCharacter characterInst = foundCharacter.get();
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
        Optional<MainCharacter> foundCharacter = this.service.findMainCharacterByUserId(loggedUserId);
        if(foundCharacter.isPresent() &&
                foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
        {
            MainCharacter characterInst = foundCharacter.get();
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
