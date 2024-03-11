package com.example.skirmishes;

import com.example.auth.AuthenticationFacade;
import com.example.auth.LoggedUserUtils;
import com.example.auth.SecuredRestController;
import com.example.battle.reports.FightReport;
import com.example.items.Item;
import com.example.items.ItemsInventoryService;
import com.example.response.CustomResponse;
import com.example.settings.Settings;
import com.example.users.User;
import com.example.users.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController("skirmishes")
public class SkirmishesController implements SecuredRestController {
    //TODO: move somewhere - maybe into user or sth
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final SkirmishesService service;
    private final ChallengesService challengesService;
    private final ItemsInventoryService itemsInventoryService;


    @Autowired
    public SkirmishesController(AuthenticationFacade authenticationFacade,
                                SkirmishesService skirmishesService,
                                UserService userService, ChallengesService challengesService,
                                ItemsInventoryService itemsInventoryService) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.service = skirmishesService;
        this.challengesService = challengesService;
        this.itemsInventoryService = itemsInventoryService;
    }

    @GetMapping("/your-skirmishes")
    public CustomResponse<Skirmish> getYourSkirmishes() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);
        return new CustomResponse<>(HttpStatus.OK, this.service.getOrCreateSkirmish(loggedUser, 2));
    }


    @GetMapping("/current-challenge")
    public CustomResponse<FightReport> getChallengeStatus() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        Skirmish foundSkirmish = this.service.getOrCreateSkirmish(loggedUser, 2);

        ChallengesService.HandleCurrentChallengeReturn returnData =
                this.challengesService.getAndHandleCurrentChallenge(foundSkirmish, loggedUser);

        if(returnData.data().isEmpty()) throw new BadRequestException(returnData.message());

        ChallengesService.CommonReturnData data = returnData.data().get();
        data.skirmish().generateChallenges(2);
        this.service.update(data.skirmish());
        //TODO: iterateCount - should be get from user collection(for example some users can have more than 2)

        this.onSuccessFight(loggedUser, data.report().getLoot(), data.report().getGainedGold());

        return new CustomResponse<>(HttpStatus.OK, returnData.message(), returnData.data().get().report());

    }
    @PostMapping("/start-challenge/{challengeId}")
    public CustomResponse<Boolean> startSkirmishChallenge(
            @PathVariable String challengeId
    ) throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        //TODO: make it from configs plusMinutes - remember(Changed for debug)
        LocalDateTime challengeFinishTimestamp = LocalDateTime.now().plusSeconds(Settings.CHALLENGE_WAIT_COOLDOWN_MINUTES);

        Skirmish foundSkirmish = this.service.getOrCreateSkirmish(loggedUser, 2);
        Skirmish.ChosenChallenge skirmishData = new Skirmish.ChosenChallenge(challengeId, challengeFinishTimestamp);

        if(foundSkirmish.getChosenChallenge() != null)
            throw new BadRequestException("Wait for a current challenge to finish");

        foundSkirmish.setChosenChallenge(skirmishData);
        this.service.update(foundSkirmish);

        return new CustomResponse<>(HttpStatus.OK, "Challenge successfully started", true);
    }

    @PostMapping("/cancel-current-challenge")
    public CustomResponse<Boolean> cancelCurrentChallenge() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        Skirmish foundSkirmish = this.service.getOrCreateSkirmish(loggedUser, 2);

        if(foundSkirmish.getChosenChallengeData().isEmpty())
            throw new BadRequestException("There is no started challenge at all");

        foundSkirmish.setChosenChallenge(null);
        this.service.update(foundSkirmish);

        return new CustomResponse<>(HttpStatus.OK, "Successfully canceled challenge", true);
    }
    @GetMapping("/dungeons")
    public CustomResponse<Dungeons> getDungeons() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);
        Skirmish skirmish = this.service.getOrCreateSkirmish(loggedUser, 2);

        return new CustomResponse<>(HttpStatus.OK, skirmish.getDungeons());
    }

    @PostMapping("/dungeons/start-a-fight/{dungeonLevel}")
    public CustomResponse<FightReport> startDungeonFight(@PathVariable int dungeonLevel) throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        Skirmish foundSkirmish = this.service.getOrCreateSkirmish(loggedUser, 2);

        ChallengesService.HandleDungeonReturn returnData =
        this.challengesService.handleDungeonFight(
                foundSkirmish, loggedUser,
                dungeonLevel, Settings.DUNGEON_WAIT_COOLDOWN_MINUTES
        );
        if(returnData.data().isEmpty()) throw new BadRequestException(returnData.message());

        ChallengesService.CommonReturnData data = returnData.data().get();
        this.service.update(data.skirmish());

        this.onSuccessFight(loggedUser, data.report().getLoot(), data.report().getGainedGold());

        return new CustomResponse<>(HttpStatus.OK, returnData.message(), returnData.data().get().report());
    }

    @PostMapping("/debug/generate-new-challenges")
    public CustomResponse<Skirmish> generateNewChallenges() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        return new CustomResponse<>(HttpStatus.OK, this.service.generateNewChallengesForUser(loggedUser, 2));
    }
    @GetMapping("/debug/delete-existing-create-debug-data")
    public CustomResponse<Skirmish> createDebug() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);

        Optional<Skirmish> foundSkirmish = this.service.findOneByUserId(loggedUser.getId());
        foundSkirmish.ifPresent(skirmish -> this.service.removeById(skirmish.getId()));
        return new CustomResponse<>(HttpStatus.OK, this.service.create(loggedUser, 2));
    }

    private void onSuccessFight(User user, List<Item> lootedItems, long gainedGold) {
        lootedItems.forEach((item)-> this.itemsInventoryService.handleOnNewUserItem(user, item));

        user.increaseGold(gainedGold);
        this.userService.update(user);
    }

}
