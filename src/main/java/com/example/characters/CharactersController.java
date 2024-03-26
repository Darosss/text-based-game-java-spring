package com.example.characters;

import com.example.auth.AuthenticationFacade;
import com.example.auth.LoggedUserUtils;
import com.example.auth.SecuredRestController;
import com.example.characters.equipment.CharacterEquipmentFieldsEnum;
import com.example.characters.equipment.Equipment.EquipItemResult;
import com.example.characters.equipment.Equipment.UnEquipItemResult;
import com.example.common.ResourceNotFoundException;
import com.example.items.*;
import com.example.response.CustomResponse;
import com.example.settings.Settings;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.statistics.StatisticsUtils;
import com.example.users.User;
import com.example.users.UserService;
import com.example.users.inventory.Inventory;
import com.example.users.inventory.InventoryService;
import com.example.utils.RandomUtils;
import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController("characters")
@RequestMapping("characters")
public class CharactersController implements SecuredRestController {
    private final CharacterService service;
    private final ItemService itemService;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final InventoryService inventoryService;

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
    public CustomResponse<List<MercenaryCharacter>> findYourMercenaries() throws Exception {
        return new CustomResponse<>(HttpStatus.OK,
                this.service.findUserMercenaries(this.authenticationFacade.getJwtTokenPayload().id())
        );
    }
    @GetMapping("/your-main-character")
    public CustomResponse<MainCharacter> findYourMainCharacter() throws Exception {
        Optional<MainCharacter> character = this.service.findMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());

        return character.map((mainChar)->new CustomResponse<>(HttpStatus.OK, mainChar))
                .orElseThrow(()->new BadRequestException("You do not have main character yet"));
    }

    //TODO; getCharactersIds and getUserCharactersIds should use CharactersService rather with sth like
    // getCharactersIdsByUserId;
    @GetMapping("/your-characters-ids")
    public CustomResponse<List<String>> getCharactersIds() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        List<String> charactersIds =loggedUser.getCharacters().stream().map((character->character.getId().toString())).toList();
        return new CustomResponse<>(HttpStatus.OK, charactersIds);
    }

    @GetMapping("/user/{userId}/characters-ids")
    public CustomResponse<List<String>> getUserCharactersIds(@PathVariable String userId) throws Exception {
        Optional<User> user = this.userService.findOneById(userId);

        return user.map((userInst)->new CustomResponse<>(HttpStatus.OK, userInst.getCharacters().stream().map((character->character.getId().toString())).toList()))
                .orElseThrow(()->new BadRequestException("User not found"));
    }

    @GetMapping("/{characterId}")
    public CustomResponse<Character> findYourCharacterById(@PathVariable String characterId) throws BadRequestException {
        Optional<Character> character = this.service.findById(characterId);

        return character.map((mainChar)->new CustomResponse<>(HttpStatus.OK, mainChar))
                .orElseThrow(()-> new BadRequestException("Character with id: "+ characterId + " not found"));
    }

    @GetMapping("debug/all")
    public CustomResponse<List<Character>> findAll()  {
        return new CustomResponse<>(HttpStatus.OK, this.service.findAll(Character.class));
    }

    @PostMapping("/create")
    public CustomResponse<MainCharacter> createMainCharacter() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        if(this.service.findMainCharacterByUserId(loggedUser.getId().toString()).isPresent()){
            throw new BadRequestException("User already have main character");
        }

        MainCharacter createdChar = service.createMainCharacter(loggedUser, loggedUser.getUsername());
        loggedUser.addCharacter(createdChar);
        this.userService.update(loggedUser);

        return new CustomResponse<>(HttpStatus.CREATED, "Successfully created main character", createdChar);
    }

    @PostMapping("/create-mercenary")
    public CustomResponse<MercenaryCharacter> createMercenaryCharacter() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        Optional<MainCharacter> mainCharacter = this.service.findMainCharacterByUserId(loggedUser.getId().toString());

        if(mainCharacter.isEmpty()) throw new Exception("Something went wrong. Not found main character");

        boolean canCreateMercenary = false;
        String errorMessage = "You have exceed a limit";
        int charactersCount = loggedUser.getCharacters().size();
        for (Settings.MercenaryCharactersLimits charsLimit : Settings.MercenaryCharactersLimits.values()) {
            if(charactersCount >= charsLimit.getCharactersLimit()) continue;
            if(mainCharacter.get().getLevel() >= charsLimit.getRequiredLevel()) {
                canCreateMercenary = true;
                break;
            }else {
                errorMessage ="You have too low level to create mercenary, need at least: "+charsLimit.getRequiredLevel();
                break;
            }
        }

        if(!canCreateMercenary) throw new BadRequestException(errorMessage);

        MercenaryCharacter createdChar = service.createMercenaryCharacter(loggedUser, "Mercenary "+(charactersCount+1));
        loggedUser.addCharacter(createdChar);
        this.userService.update(loggedUser);
        return new CustomResponse<>(HttpStatus.CREATED, "Successfully created mercenary character", createdChar);


    }

    // For debug ignore ;p
    @PostMapping("/debug/create-empty-char/{userId}/{name}/{asMainCharacter}")
    public CustomResponse<Character> create(@PathVariable String userId,
                            @PathVariable String name,
                            @PathVariable boolean asMainCharacter
    ){
        Optional<User> foundUser = this.userService.findOneById(userId);

        if(foundUser.isEmpty()) throw new ResourceNotFoundException("User", userId);


        if(asMainCharacter) {
            return new CustomResponse<>(HttpStatus.CREATED, "Successfully created main character character", this.service.createMainCharacter(foundUser.get(), name));
        }else {
            return new CustomResponse<>(HttpStatus.CREATED, "Successfully created mercenary character", this.service.createMercenaryCharacter(foundUser.get(), name));
        }
    }

    @PostMapping("/debug/create-mercenary-character-with-random-stats/{name}")
        public CustomResponse<MercenaryCharacter> createDebugMercenary(
                @PathVariable String name
    ) throws Exception {
            User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);
            MercenaryCharacter createdChar = service.createMercenaryCharacter(
                    loggedUser, name
            );
            loggedUser.addCharacter(createdChar);
            this.userService.update(loggedUser);
            return new CustomResponse<>(HttpStatus.CREATED, "Successfully created mercenary character", createdChar);
        }

    @PostMapping("/debug/create-main-character-with-random-stats/{name}")
    public CustomResponse<MainCharacter> createDebug(
            @PathVariable String name
    ) throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);
        MainCharacter createdChar = service.createDebugCharacter(
                loggedUser,
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

            loggedUser.addCharacter(createdChar);
            this.userService.update(loggedUser);
            return new CustomResponse<>(HttpStatus.CREATED, "Successfully created main character with random stats", createdChar);
    }
    @PostMapping("/use-consumable/{itemId}")
    public CustomResponse<Boolean> useConsumable(
            @PathVariable String itemId
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();

        Optional<MainCharacter> mainCharacter = this.service.findMainCharacterByUserId(loggedUserId);
        Optional<ItemConsumable> itemToUse = this.itemService.findOne(itemId, ItemConsumable.class);

        Inventory inventory = this.inventoryService.getUserInventory(loggedUserId);

        if(itemToUse.isEmpty()) throw new ResourceNotFoundException("Item", itemId);
        if(mainCharacter.isEmpty()) throw new ResourceNotFoundException("Main character does not exist");

        boolean usedItem = this.characterInventoryService.useConsumableItem(inventory, mainCharacter.get(),  itemToUse.get());
        if(usedItem) return new CustomResponse<>(HttpStatus.OK, "Successfully used item", true);

        //TODO: add here better messages through service. Like in equip un equip methods
        throw new BadRequestException("Cannot use consumable item");
    };
    @PostMapping("/equip/{characterId}/{itemId}/{slot}")
    public CustomResponse<Boolean> equipCharacterItem(
            @PathVariable String characterId,
            @PathVariable String itemId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<ItemWearable> itemToEquip = this.itemService.findOne(itemId, ItemWearable.class);

        if(itemToEquip.isEmpty()) throw new ResourceNotFoundException("Item", itemId);

        EquipItemResult returnData = this.characterInventoryService.equipItem(
                new ObjectId(loggedUserId),
                new ObjectId(characterId),
                itemToEquip.get(), slot
        );

        if(returnData.success()) return new CustomResponse<>(HttpStatus.OK, returnData.message(), true);

        throw new BadRequestException(returnData.message());
    }

    @PostMapping("/un-equip/{characterId}/{slot}")
    public CustomResponse<Item> unEquipCharacterItem(
            @PathVariable String characterId,
            @PathVariable CharacterEquipmentFieldsEnum slot
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();

        UnEquipItemResult returnData = this.characterInventoryService.unEquipItem(new ObjectId(loggedUserId),  new ObjectId(characterId), slot);
        if(returnData.success() && returnData.item().isPresent()) return new CustomResponse<>(HttpStatus.OK, returnData.message(), returnData.item().get());

        throw new BadRequestException(returnData.message());
    }


    @PostMapping("/equip-mercenary/{characterId}/{itemId}")
    public CustomResponse<Boolean> equipMercenary(
            @PathVariable String characterId,
            @PathVariable String itemId
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<ItemMercenary> itemToEquip = this.itemService.findOne(itemId, ItemMercenary.class);

        if(itemToEquip.isEmpty()) throw new ResourceNotFoundException("Mercenary item", itemId);

        EquipItemResult returnData = this.characterInventoryService.useMercenaryItemOnMercenaryCharacter(
                new ObjectId(loggedUserId), new ObjectId(characterId), itemToEquip.get());

        if(returnData.success()) return new CustomResponse<>(HttpStatus.OK, returnData.message(), true);

        throw new BadRequestException(returnData.message());
    }
    @PostMapping("/un-equip-mercenary/{characterId}")
    public CustomResponse<Item> unEquipMercenary(
            @PathVariable String characterId
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();

        UnEquipItemResult returnData = this.characterInventoryService.unEquipMercenaryItemFromMercenaryCharacter(
                new ObjectId(loggedUserId), new ObjectId(characterId)
        );
        if(returnData.success() && returnData.item().isPresent()) return new CustomResponse<>(HttpStatus.OK, returnData.message(), returnData.item().get());

        throw new BadRequestException(returnData.message());

    }
    @PatchMapping("/train-statistic/{statisticName}/{addValue}")
    public CustomResponse<Boolean> trainStatistic(@PathVariable BaseStatisticsNamesEnum statisticName,
                                  @PathVariable int addValue
                                  ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<MainCharacter> foundCharacter = this.service.findMainCharacterByUserId(loggedUserId);

        if(foundCharacter.isEmpty()) throw new ResourceNotFoundException("Main character not found");
        if(!foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
            throw new BadRequestException("You can train only your character");

        MainCharacter characterInst = foundCharacter.get();
        characterInst.getStats().increaseStatistic(statisticName, addValue, StatisticsUtils.StatisticUpdateType.VALUE);
        this.service.update(characterInst);
        return new CustomResponse<>(HttpStatus.OK, "Successfully trained statistic", true);
    }

    @PatchMapping("/debug/subtract-statistic/{statisticName}/{subtractValue}")
    public CustomResponse<Boolean> debugSubtractTrainStatistic(@PathVariable BaseStatisticsNamesEnum statisticName,
                                  @PathVariable int subtractValue
    ) throws Exception {
        String loggedUserId = this.authenticationFacade.getJwtTokenPayload().id();
        Optional<MainCharacter> foundCharacter = this.service.findMainCharacterByUserId(loggedUserId);

        if(foundCharacter.isEmpty()) throw new ResourceNotFoundException("Main character not found");
        if(!foundCharacter.get().getUser().getId().equals(new ObjectId(loggedUserId)))
           throw new BadRequestException("You can only subtract stats from your character");

        MainCharacter characterInst = foundCharacter.get();
        characterInst.getStats().decreaseStatistic(statisticName, subtractValue, StatisticsUtils.StatisticUpdateType.VALUE);
        this.service.update(characterInst);
        return new CustomResponse<>(HttpStatus.OK, "Successfully subtracted statistic",true);

    }

    //TODO: remove this endpoint.
    @DeleteMapping("debug/delete-all")
    public CustomResponse<Boolean> deleteAllItems() {
        this.service.removeAllCharactersAndEquipments();

        return new CustomResponse<>(HttpStatus.OK, "[DEBUG]: Successfully removed all characters", true);
    }
}
