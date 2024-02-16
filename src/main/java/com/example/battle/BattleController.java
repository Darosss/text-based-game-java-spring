package com.example.battle;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.battle.reports.FightReport;
import com.example.characters.CharacterService;
import com.example.characters.MainCharacter;
import com.example.enemies.Enemy;
import com.example.enemies.EnemyType;
import com.example.enemies.EnemyUtils;
import com.example.skirmishes.EnemySkirmishDifficulty;
import com.example.utils.RandomUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;


@RestController("battle")
public class BattleController implements SecuredRestController {
    private final BattleManagerService battleManagerService;
    private final AuthenticationFacade authenticationFacade;
    private final CharacterService characterService;
    private final int MIN_HEALTH_FOR_FIGHT = 20;

    private record IntRange(int min, int max){}

    @Autowired
    BattleController(BattleManagerService battleManagerService,
                     CharacterService characterService,
                     AuthenticationFacade authenticationFacade
                     ){
        this.battleManagerService = battleManagerService;
        this.characterService = characterService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/debug/attack/{enemyType}")
    public FightReport DebugAttack(
            @PathVariable EnemyType enemyType
    ) throws Exception {
        Optional<MainCharacter> mainCharacter = this.characterService.findMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());
        int randomLevel = RandomUtils.getRandomValueWithinRange(1,100);


        if(mainCharacter.isPresent()){
            Enemy enemy = EnemyUtils.createRandomEnemyBasedOnLevel(enemyType+" enemy",
                    randomLevel, enemyType, EnemyUtils.getEnemyStatsMultiplier(enemyType)
                    );

            MainCharacter characterInst = mainCharacter.get();
            FightReport report =  this.battleManagerService.performFight(characterInst, enemy);

            characterInst.gainExperience(report.getGainedExperience());
            this.characterService.update(characterInst);
            return report;
        }
        return null;
    }
    @PostMapping("/debug/attack/{enemyLevel}/{enemyType}")
    public FightReport DebugAttackWithEnemyStrength(
            @PathVariable EnemySkirmishDifficulty difficulty,
            @PathVariable EnemyType enemyType
    ) throws Exception {
        Optional<MainCharacter> mainCharacter = this.characterService.findMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());

        if(mainCharacter.isPresent()){
            MainCharacter characterInst = mainCharacter.get();
            if(characterInst.getHealth() <= MIN_HEALTH_FOR_FIGHT) throw new BadRequestException("You cannot attack with low health points. Need at least: " + this.MIN_HEALTH_FOR_FIGHT);
            EnemyUtils.LevelRange enemyRange = EnemyUtils.getEnemyLevelRanges(difficulty, characterInst.getLevel());

            Enemy enemy = EnemyUtils.createRandomEnemyBasedOnLevel(
             enemyType + " enemy",
                    RandomUtils.getRandomValueWithinRange(enemyRange.min(), enemyRange.max()),
                    enemyType, EnemyUtils.getEnemyStatsMultiplier(enemyType)
                );

            FightReport report =  this.battleManagerService.performFight(characterInst, enemy);

            characterInst.gainExperience(report.getGainedExperience());
            this.characterService.update(characterInst);
            return report;
        }
        return null;
    }

//    @PostMapping("/debug/attack-many-characters")
//    public FightReport DebugAttackMultipleCharacters() throws Exception {
//        List<Character> foundCharacters = this.characterService.findUserCharacters(this.authenticationFacade.getJwtTokenPayload().id());
//        int randomLevel = RandomUtils.getRandomValueWithinRange(1,100);
//        EnemyType randomEnemyType = EnemyUtils.getEnemyTypeDependsOnProbability();
//        double statsMultiplier = EnemyUtils.getEnemyStatsMultiplier(randomEnemyType);
//        List<Enemy> enemies =  List.of(
//                EnemyUtils.createRandomEnemyBasedOnLevel("Enemy", randomLevel, randomEnemyType, statsMultiplier));
//            if(!foundCharacters.isEmpty()){
//            return this.battleManagerService.performNormalFight(foundCharacters, enemies);
//        }
//        return null;
//    }
//    @PostMapping("/debug/many-vs-many")
//    public FightReport DebugManyVsMany() throws Exception {
//        List<Character> foundCharacters = this.characterService.findUserCharacters(this.authenticationFacade.getJwtTokenPayload().id());
//        List<Enemy> enemies =  List.of(
//            EnemyUtils.createRandomEnemyBasedOnLevel("Enemy number prob 5.0, stats 2", 20, EnemyUtils.getEnemyTypeDependsOnProbability(5.0),2),
//            EnemyUtils.createRandomEnemyBasedOnLevel("Enemy number prob 2.0, stats 1.2",25, EnemyUtils.getEnemyTypeDependsOnProbability(2.0) , 1.2),
//            EnemyUtils.createRandomEnemyBasedOnLevel("Enemy number stats prob 1 0.8", 25,EnemyUtils.getEnemyTypeDependsOnProbability(), 0.8)
//        );
//        if(!foundCharacters.isEmpty()){
//            return this.battleManagerService.performNormalFight(foundCharacters, enemies);
//        }
//        return null;
//    }

}
