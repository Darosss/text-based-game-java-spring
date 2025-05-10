package com.textbasedgame.users.inventory;

import com.textbasedgame.auth.AuthenticationFacade;
import com.textbasedgame.auth.SecuredRestController;
import com.textbasedgame.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("inventories")
public class InventoriesController implements SecuredRestController {
    private final AuthenticationFacade authenticationFacade;
    private final InventoryService service;

    @Autowired
    public InventoriesController(AuthenticationFacade authenticationFacade, InventoryService service) {
        this.authenticationFacade = authenticationFacade;
        this.service = service;
    }

    @GetMapping("/your-inventory")
    public CustomResponse<Inventory> getLoggedUserInventory() throws Exception {
        return new CustomResponse<>(HttpStatus.OK,
                this.service.getUserInventory(this.authenticationFacade.getJwtTokenPayload().id()));
    }

}
