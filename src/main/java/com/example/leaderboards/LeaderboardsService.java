package com.example.leaderboards;

import com.example.characters.BaseHero;
import com.example.characters.CharacterInventoryService;
import com.example.characters.CharacterService;
import com.example.characters.MainCharacter;
import com.example.settings.Settings;
import com.example.skirmishes.Skirmish;
import com.example.skirmishes.SkirmishesService;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LeaderboardsService {
    private static final Logger logger = LoggerFactory.getLogger(CharacterInventoryService.class);

    private final CharacterService characterService;
    private final SkirmishesService skirmishesService;
    private final Datastore datastore;
    private LocalDateTime nextRefreshAt;
    @Autowired
    public LeaderboardsService(Datastore datastore, CharacterService characterService, SkirmishesService skirmishesService){
        this.datastore = datastore;
        this.characterService = characterService;
        this.skirmishesService = skirmishesService;
    }

    @Scheduled(initialDelay = 0, fixedRate = Settings.LEADERBOARD_REFRESH_COOLDOWN_MS)
    private void updateLeaderboardScheduled() {
        for (Leaderboard.LeaderboardCategory cat : Leaderboard.LeaderboardCategory.values()){
            Leaderboard leaderboard = this.getOrCreateLeaderboardByCategory(cat);
            List<Leaderboard.LeaderboardData> dataForLevels = this.calculateLeaderboardData(cat);
            leaderboard.setData(dataForLevels);
            this.update(leaderboard);
            logger.debug("Update cat leaderboard: {}", cat);
        }
        this.nextRefreshAt = LocalDateTime.now().plus(Duration.ofMillis(Settings.LEADERBOARD_REFRESH_COOLDOWN_MS));
    }

    public LocalDateTime getNextRefreshAt() {
        return nextRefreshAt;
    }

    private List<Leaderboard.LeaderboardData> calculateLeaderboardData(Leaderboard.LeaderboardCategory category) {
        return switch(category){
            case LEVELS -> this.updateLevelsLeaderboard();
            case DUNGEONS -> this.updateDungeonsLeaderboard();
            default -> new ArrayList<>();
        };
    }

    private List<Leaderboard.LeaderboardData> updateLevelsLeaderboard() {
        List<MainCharacter> mainCharacters = new ArrayList<>(this.characterService.findAll(MainCharacter.class));


        if(mainCharacters.size() > 1) mainCharacters.sort(
                Comparator.comparingInt(BaseHero::getLevel).reversed());

        List<Leaderboard.LeaderboardData> leaderboardData = new ArrayList<>();
        int place = 1;
        for (MainCharacter character : mainCharacters) {
            leaderboardData.add(new Leaderboard.LeaderboardData(place++,
                    character.getUser().getId(), character.getUser().getUsername(), character.getLevel()));
        }

        return leaderboardData;
    }

    private List<Leaderboard.LeaderboardData> updateDungeonsLeaderboard() {
        List<Skirmish> skirmishes = new ArrayList<>(this.skirmishesService.findAll());

        skirmishes.sort(Comparator.
                <Skirmish>comparingInt(skirmish -> skirmish.getDungeons().getCurrentLevel()).reversed()
        );

        List<Leaderboard.LeaderboardData> leaderboardData = new ArrayList<>();
        int place = 1;
        for (Skirmish skirmish : skirmishes) {
            leaderboardData.add(new Leaderboard.LeaderboardData(place++,
                    skirmish.getUser().getId(), skirmish.getUser().getUsername(), skirmish.getDungeons().getCurrentLevel()));
        }

        return leaderboardData;
    }

    private Optional<Leaderboard> findOneLeaderboardByCategory(Leaderboard.LeaderboardCategory category) {
        return Optional.ofNullable(datastore.find(Leaderboard.class).filter(Filters.eq("category", category)).first());
    }

    private Leaderboard getOrCreateLeaderboardByCategory(Leaderboard.LeaderboardCategory category){
        Optional<Leaderboard> foundSkirmish = this.findOneLeaderboardByCategory(category);
        return foundSkirmish.orElseGet(() -> this.create(category));
    }
    private Leaderboard create(Leaderboard leaderboard) {
        return datastore.save(leaderboard);
    }
    private Leaderboard create(Leaderboard.LeaderboardCategory category) {
        return datastore.save(new Leaderboard(category, new ArrayList<>()));
    }
    private Leaderboard update(Leaderboard leaderboard) {
        return datastore.save(leaderboard);
    }

}
