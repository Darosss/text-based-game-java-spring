package com.example.merchants;


import com.example.auth.AuthenticationFacade;
import com.example.auth.LoggedUserUtils;
import com.example.auth.SecuredRestController;
import com.example.characters.CharacterService;
import com.example.characters.MainCharacter;
import com.example.items.Item;
import com.example.response.CustomResponse;
import com.example.users.User;
import com.example.users.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//TODO: add refreshing commodity after time
@RestController("merchants")
@RequestMapping("merchants")
public class MerchantsController implements SecuredRestController {
    private final AuthenticationFacade authenticationFacade;
    private final MerchantsService service;
    private final CharacterService characterService;
    private final UserService userService;

    @Autowired
    public MerchantsController(AuthenticationFacade authenticationFacade, MerchantsService service, CharacterService characterService, UserService userService) {
        this.authenticationFacade = authenticationFacade;
        this.service = service;
        this.characterService = characterService;
        this.userService = userService;
    }

    @GetMapping("/your-merchant")
    public CustomResponse<Merchant> getMerchant() throws Exception {
        User loggedUser = LoggedUserUtils.getLoggedUserDetails(this.authenticationFacade, this.userService);
        Optional<MainCharacter> mainCharacter = this.characterService.findMainCharacterByUserId(loggedUser.getId());

        if(mainCharacter.isEmpty()) throw new BadRequestException("You need to create main character, before visit merchant");

        List<Item> items = MerchantsUtils.generateMerchantsItems(mainCharacter.get().getLevel());

        Merchant merchant = this.service.getOrCreateMerchant(loggedUser, items);

        return new CustomResponse<>(HttpStatus.OK, merchant);
    }

    @PostMapping("/buy-item/{itemId}")
    public CustomResponse<Item> buyItemFromMerchant(
            @PathVariable String itemId
    ) throws Exception {
        MerchantsService.MerchantActionReturn returnData = this.service.buyItemFromMerchant(
                this.authenticationFacade.getJwtTokenPayload().id(), itemId);
        if(returnData.success() && returnData.item().isPresent())
            return new CustomResponse<>(HttpStatus.OK, returnData.message(), returnData.item().get());
        throw new BadRequestException(returnData.message());
    }

    @PostMapping("/sell-item/{itemId}")
    public CustomResponse<Item> sellItemToMerchant(
            @PathVariable String itemId
    ) throws Exception {
        MerchantsService.MerchantActionReturn returnData = this.service.sellItemToMerchant(
                this.authenticationFacade.getJwtTokenPayload().id(), itemId);
        if(returnData.success() && returnData.item().isPresent())
            return new CustomResponse<>(HttpStatus.OK, returnData.message(), returnData.item().get());
        throw new BadRequestException(returnData.message());
    }


}
