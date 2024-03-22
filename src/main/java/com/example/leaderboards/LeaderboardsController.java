package com.example.leaderboards;

import com.example.auth.SecuredRestController;
import com.example.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController("leaderboards")
@RequestMapping("leaderboards")
public class LeaderboardsController implements SecuredRestController {
    private final LeaderboardsService service;

    @Autowired
    public LeaderboardsController(LeaderboardsService leaderboardsService){
        this.service = leaderboardsService;
    }

    @GetMapping("/next-refresh-time")
    public CustomResponse<LocalDateTime> findYourMercenaries() throws Exception {
        return new CustomResponse<>(HttpStatus.OK,this.service.getNextRefreshAt());
    }
}
