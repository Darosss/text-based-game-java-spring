package com.example.skirmishes;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.battle.reports.FightReport;
import com.example.items.ItemService;
import com.example.users.User;
import com.example.users.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController("skirmishes")
public class SkirmishesController implements SecuredRestController {
    //TODO: move somewhere - maybe into user or sth
    private final long CHALLENGE_WAIT_TIME_MINUTES = 1;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final SkirmishesService service;
    private final ChallengesService challengesService;
    private final ItemService itemService;


    @Autowired
    public SkirmishesController(AuthenticationFacade authenticationFacade,
                                SkirmishesService skirmishesService,
                                UserService userService, ChallengesService challengesService,
                                ItemService itemService) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.service = skirmishesService;
        this.challengesService = challengesService;
        this.itemService = itemService;

    }

    @GetMapping("/your-skirmishes")
    public Skirmish getYourSkirmishes() throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isEmpty()) return null;

        User userInstance = foundUser.get();

        return this.service.getOrCreateSkirmish(userInstance, 2);
    }


    @GetMapping("/current-challenge")
    public FightReport getChallengeStatus() throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isEmpty()) return null;
        String userId = this.authenticationFacade.getJwtTokenPayload().id();
        Skirmish foundSkirmish = this.service.getOrCreateSkirmish(foundUser.get(), 2);

        Optional<ChallengesService.HandleCurrentChallengeReturn> returnData =
                this.challengesService.getAndHandleCurrentChallenge(foundSkirmish, userId);

        if(returnData.isEmpty()) return null; //add message + handle

        ChallengesService.HandleCurrentChallengeReturn returnDataInst = returnData.get();

        //TODO: iterateCount - should be get from user collection(for example some users can have more than 2)
        returnDataInst.skirmish().generateChallenges(2);
        this.service.update(returnDataInst.skirmish());

        returnDataInst.report().getLoot().forEach((item)->{
            item.setUser(foundUser.get());
            this.itemService.create(item);
        });
        return returnDataInst.report();
    }
    @PostMapping("/start-challenge/{challengeId}")
    public Skirmish.ChosenChallenge startSkirmishChallenge(
            @PathVariable String challengeId
    ) throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isEmpty()) return null;
        LocalDateTime challengeFinishTimestamp = LocalDateTime.now().plusSeconds(this.CHALLENGE_WAIT_TIME_MINUTES);
        User userInstance = foundUser.get();

        Skirmish foundSkirmish = this.service.getOrCreateSkirmish(userInstance, 2);
        Skirmish.ChosenChallenge skirmishData = new Skirmish.ChosenChallenge(challengeId, challengeFinishTimestamp);

        if(foundSkirmish.getChosenChallenge() != null)
            throw new BadRequestException("Wait for a current challenge to finish");

        foundSkirmish.setChosenChallenge(skirmishData);
        this.service.update(foundSkirmish);

        return skirmishData;
    }

    @PostMapping("/cancel-current-challenge")
    public Skirmish cancelCurrentChallenge() throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isEmpty()) return null;

        Skirmish foundSkirmish = this.service.getOrCreateSkirmish(foundUser.get(), 2);

        if(foundSkirmish.getChosenChallengeData().isEmpty()){
            throw new BadRequestException("There is no started challenge to cancel");
        }

        foundSkirmish.setChosenChallenge(null);

        return this.service.update(foundSkirmish);
    }


    @PostMapping("/debug/generate-new-challenges")
    public Skirmish generateNewChallenges() throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isEmpty()) return null;

        return this.service.generateNewChallengesForUser(foundUser.get(), 2);

    }
    @GetMapping("/debug/delete-existing-create-debug-data")
    public Skirmish createDebug() throws Exception {
        Optional<User> foundUser = this.userService.findOneById(this.authenticationFacade.getJwtTokenPayload().id());
        if(foundUser.isEmpty()) return null;

        User userInstance = foundUser.get();
        Optional<Skirmish> foundSkirmish = this.service.findOneByUserId(userInstance.getId());
        foundSkirmish.ifPresent(skirmish -> this.service.removeById(skirmish.getId()));
        return this.service.create(userInstance, 2);
    }


}
