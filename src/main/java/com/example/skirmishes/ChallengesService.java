package com.example.skirmishes;

import com.example.battle.BattleManagerService;
import com.example.battle.reports.FightReport;
import com.example.characters.Character;
import com.example.characters.CharacterService;
import com.example.enemies.Enemy;
import com.example.enemies.EnemyType;
import com.example.enemies.EnemyUtils;
import com.example.utils.RandomUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ChallengesService {
    private final BattleManagerService battleManagerService;
    private final CharacterService characterService;

    public record HandleCurrentChallengeReturn (Skirmish skirmish, FightReport report){}

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

    private Character getUserMainCharacter(String userId) throws Exception {
        Optional<Character> foundCharacter = this.characterService.findOneMainCharacterByUserId(userId);
        if(foundCharacter.isEmpty()) throw new Exception("Something went wrong - no found character");

        return foundCharacter.get();
    }

    private FightReport completeFinishedChallenge(String userId, Skirmish skirmish) throws Exception {
        Optional<SkirmishData> challenge = skirmish.getChosenChallengeData();

        if(challenge.isEmpty()) return null;

        Character mainCharacter = this.getUserMainCharacter(userId);

        Enemy createdEnemy = this.prepareChallengeEnemy(challenge.get().getDifficulty(), mainCharacter.getLevel());

        FightReport fightReport = this.battleManagerService.performFight(mainCharacter, createdEnemy);
        this.handleOnFinishFight(fightReport, mainCharacter);


        return fightReport;
    }

    private void handleOnFinishFight(FightReport report, Character mainCharacter){
        mainCharacter.gainExperience(report.getGainedExperience());
        //TODO: add gold, items etc


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

}
