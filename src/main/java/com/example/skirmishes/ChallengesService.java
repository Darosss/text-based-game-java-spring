package com.example.skirmishes;

import com.example.battle.BattleManagerService;
import com.example.battle.reports.FightReport;
import com.example.characters.Character;
import com.example.characters.CharacterService;
import com.example.characters.MainCharacter;
import com.example.characters.MercenaryCharacter;
import com.example.enemies.Enemy;
import com.example.enemies.EnemyType;
import com.example.enemies.EnemyUtils;
import com.example.utils.RandomUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengesService {
    private final BattleManagerService battleManagerService;
    private final CharacterService characterService;

    public record HandleCurrentChallengeReturn (Skirmish skirmish, FightReport report){}
    public record HandleDungeonReturn (Skirmish skirmish, FightReport report){}

    @Autowired
    public ChallengesService(BattleManagerService battleManagerService, CharacterService characterService) {
        this.battleManagerService = battleManagerService;
        this.characterService = characterService;
    }

    public Optional<HandleCurrentChallengeReturn> getAndHandleCurrentChallenge(Skirmish skirmish, String userId) throws Exception {
        Skirmish.ChosenChallenge chosenChallenge = skirmish.getChosenChallenge();

        //Note: I'm not sure that throwing an error here is good idea. - think later
        if(chosenChallenge == null) throw new BadRequestException("There is no current challenges at all");

        if(!skirmish.isChallengeTimeCompleted()) return Optional.empty();


        FightReport fightReport = this.completeFinishedChallenge(userId, skirmish);



        return Optional.of(new HandleCurrentChallengeReturn(skirmish, fightReport));
    }

    public Optional<HandleDungeonReturn> handleDungeonFight(Skirmish skirmish, String userId, int dungeonLevel, int waitTime) throws Exception {
        Dungeons dungeons = skirmish.getDungeons();
        if(!skirmish.getDungeons().canStartFight()) return Optional.empty(); //TODO: add cannot start fight and return date when can
        if(dungeonLevel <=0 || dungeonLevel > dungeons.getCurrentLevel()) return Optional.empty(); // TODO: add cannot start fight because level is dungeonLevel = too small || too high

        FightReport fightReport = this.completeStartedDungeonFight(userId, dungeonLevel);

        LocalDateTime currentTime = LocalDateTime.now();
        //TODO: change to plusMinutes - changed for plusSeconds for debug
        dungeons.setCanFightDate(currentTime.plusSeconds(waitTime));
        if(dungeonLevel == dungeons.getCurrentLevel()) {
            dungeons.addCompletedDungeon(new Dungeons.DungeonData(dungeons.getCurrentLevel(), currentTime));
            dungeons.increaseCurrentLevel();
        }

        return Optional.of(new HandleDungeonReturn(skirmish, fightReport));
    }



    private MainCharacter getUserMainCharacter(String userId) throws Exception {
        Optional<MainCharacter> foundCharacter = this.characterService.findMainCharacterByUserId(userId);
        if(foundCharacter.isEmpty()) throw new Exception("Something went wrong - no found character");

        return foundCharacter.get();
    }
    private List<MercenaryCharacter> getUserMercenariesCharacters(String userId)  {

        return this.characterService.findUserMercenaries(userId);
    }

    private FightReport completeFinishedChallenge(String userId, Skirmish skirmish) throws Exception {
        Optional<SkirmishData> challenge = skirmish.getChosenChallengeData();

        if(challenge.isEmpty()) return null;

        MainCharacter mainCharacter = this.getUserMainCharacter(userId);

        Enemy createdEnemy = this.prepareChallengeEnemy(challenge.get().getDifficulty(), mainCharacter.getLevel());

        FightReport fightReport = this.battleManagerService.performFight(mainCharacter, createdEnemy);
        this.handleOnFinishFight(fightReport, mainCharacter);

        return fightReport;
    }

    private FightReport completeStartedDungeonFight(String userId, int dungeonLevel) throws Exception {
        MainCharacter mainCharacter = this.getUserMainCharacter(userId);
        List<MercenaryCharacter> mercenaries = this.getUserMercenariesCharacters(userId);
        List<Enemy> createdEnemies = this.prepareDungeonsEnemies(dungeonLevel);

        List<Character> characters = new ArrayList<>(mercenaries);
        characters.add(mainCharacter);

        FightReport fightReport = this.battleManagerService.performTeamFight(characters, createdEnemies);

        this.handleOnFinishFight(fightReport, mainCharacter);
        return fightReport;
    }

    private void handleOnFinishFight(FightReport report, MainCharacter mainCharacter){
        mainCharacter.gainExperience(report.getGainedExperience());

        this.characterService.update(mainCharacter);
    }


    private Enemy prepareChallengeEnemy(EnemySkirmishDifficulty difficulty, int playerLevel){
        EnemyUtils.LevelRange levelRanges = EnemyUtils.getEnemyLevelRanges(difficulty, playerLevel);
        EnemyType enemyType = EnemyUtils.getEnemyTypeBasedOnSkirmishDifficulty(difficulty);
        return EnemyUtils.createRandomEnemyBasedOnLevel(
                "Challenge enemy "+enemyType , RandomUtils.getRandomValueWithinRange(levelRanges.min(), levelRanges.max()),
                enemyType, EnemyUtils.getEnemyStatsMultiplier(enemyType)
        );
    }
    private List<Enemy> prepareDungeonsEnemies(int dungeonLevel){
        List<Enemy> enemies = new ArrayList<>();

        int countOfEnemies = EnemyUtils.getCountOfDungeonEnemiesBasedOnDungeonLevel(dungeonLevel);
        for(int i = 0; i < countOfEnemies; i++){
            int enemyLevel = RandomUtils.getRandomValueWithinRange(
                    Math.max(1, dungeonLevel - 5), dungeonLevel+5
            );
            EnemyType enemyType = EnemyUtils.getEnemyTypeBasedOnDungeonLevel(dungeonLevel);
            enemies.add(EnemyUtils.createRandomEnemyBasedOnLevel(
                    "Dungeon enemy "+enemyType, enemyLevel,
                    enemyType, EnemyUtils.getEnemyStatsMultiplier(enemyType)
            ));
        }

        return enemies;
    }

}
