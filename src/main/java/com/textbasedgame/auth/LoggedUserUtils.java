package com.textbasedgame.auth;

import com.textbasedgame.characters.MainCharacter;
import com.textbasedgame.common.ResourceNotFoundException;
import com.textbasedgame.users.User;
import com.textbasedgame.users.UserService;

import java.util.Optional;

public class LoggedUserUtils {

    private LoggedUserUtils() {
    }


    public record MainCharacterDetails (int health, int maxHealth, int level, long experience, long neededExp){}
    public record ProfileUserDetails(User user, Optional<MainCharacterDetails> heroDetails){}
    public static User getLoggedUserDetails(AuthenticationFacade authenticationFacade, UserService userService) throws Exception {
        String userId = authenticationFacade.getJwtTokenPayload().id();
        Optional<User> foundUser = userService.findOneById(userId);
        return foundUser.orElseThrow(() -> new ResourceNotFoundException("Not found user data. Please try again later"));
    }
    public static ProfileUserDetails getProfileUserDetails(AuthenticationFacade authenticationFacade, UserService userService) throws Exception {
        User user = getLoggedUserDetails(authenticationFacade, userService);
        return new ProfileUserDetails(user, getMainCharacterDetails(user));
    }

    public static Optional<MainCharacterDetails> getMainCharacterDetails(User user) {
        Optional<MainCharacter>  mainCharacter = user.getMainCharacter();
        return mainCharacter.map((character)->new MainCharacterDetails(
                character.getHealth(), character.getStats().getMaxHealthEffValue(), character.getLevel(), character.getExperience(), character.getExpToLevelUp()
        ));
    }
}