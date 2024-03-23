package com.example.leaderboards;

import com.example.auth.SecuredRestController;
import com.example.response.CustomResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController("leaderboards")
@RequestMapping("leaderboards")
public class LeaderboardsController implements SecuredRestController {
    private final LeaderboardsService service;

    @Autowired
    public LeaderboardsController(LeaderboardsService leaderboardsService){
        this.service = leaderboardsService;
    }

    @GetMapping("/next-refresh-time")
    public CustomResponse<LocalDateTime> getNextRefreshTime()  {
        return new CustomResponse<>(HttpStatus.OK,this.service.getNextRefreshAt());
    }
    @GetMapping("/category/{category}")
    public CustomResponse<Leaderboard> findLeaderboardByCategory(
            @PathVariable Leaderboard.LeaderboardCategory category
    ) throws BadRequestException {
        Optional<Leaderboard> leaderboard = this.service.findOneLeaderboardByCategory(category);
        return leaderboard.map((board)->new CustomResponse<>(HttpStatus.OK, board))
                .orElseThrow(()->new BadRequestException("Not found leaderboard with category: "+category));
    }

}
