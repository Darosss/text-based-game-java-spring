package com.example.battle;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.battle.reports.FightReport;
import com.example.characters.Character;
import com.example.characters.CharacterService;
import com.example.enemies.Enemy;
import com.example.enemies.EnemyService;
import com.example.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;


@RestController("battle")
public class BattleController implements SecuredRestController {
    private BattleManagerService battleManagerService;
    private final AuthenticationFacade authenticationFacade;
    private CharacterService characterService;
    private EnemyService enemyService;


    public enum EnemyLevelChoice {
        NOOB, WEAKER, EQUAL, FAIR, STRONGER, HARDEST
    }

    private record IntRange(int min, int max){}

    @Autowired
    BattleController(BattleManagerService battleManagerService,
                     CharacterService characterService,
                     EnemyService enemyService,
                     AuthenticationFacade authenticationFacade

                     ){
        this.battleManagerService = battleManagerService;
        this.characterService = characterService;
        this.enemyService = enemyService;
        this.authenticationFacade = authenticationFacade;
    }

    private IntRange getEnemyLevelsRange(EnemyLevelChoice strength, int characterLevel){
        return switch (strength){
            case NOOB -> new IntRange(Math.max(1,characterLevel-40), characterLevel-10 );
            case WEAKER -> new IntRange(Math.max(1, characterLevel-10), Math.max(1, characterLevel-2) );
            case EQUAL -> new IntRange(characterLevel, characterLevel);
            case FAIR -> new IntRange(Math.max(1, characterLevel-1),Math.max(1, characterLevel+1) );
            case STRONGER -> new IntRange(characterLevel+5, characterLevel+10);
            case HARDEST -> new IntRange(characterLevel+10, characterLevel+50);
        };
    }

    private double getEnemyStatsMultipler(Enemy.EnemyType type){
        return switch (type){
            case EASY -> 0.5;
            case NORMAL -> 1;
            case MEDIUM -> 1.2;
            case HARD -> 2;
            case IMPOSSIBLE -> 3;
        };
    }
    @PostMapping("/debug/attack/{enemyType}")
    public FightReport DebugAttack(
            @PathVariable Enemy.EnemyType enemyType
    ) throws Exception {
        Optional<Character> foundCharacter = this.characterService.findOneMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());
        int randomLevel = RandomUtils.getRandomValueWithinRange(1,100);
        List<Enemy> enemies =  List.of(
                this.enemyService.createRandomEnemyBasedOnLevel(randomLevel,enemyType,2
                ));
        if(foundCharacter.isPresent()){
            Character characterInst = foundCharacter.get();
            FightReport report =  this.battleManagerService.performNormalFight(List.of(characterInst), enemies);

            characterInst.gainExperience(report.getGainedExperience());
            this.characterService.update(characterInst);
            return report;
        }
        return null;
    }
    @PostMapping("/debug/attack/{enemyLevel}/{enemyType}")
    public FightReport DebugAttackWithEnemyStrength(
            @PathVariable EnemyLevelChoice enemyLevel,
            @PathVariable Enemy.EnemyType enemyType
    ) throws Exception {
        Optional<Character> foundCharacter = this.characterService.findOneMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());

        if(foundCharacter.isPresent()){
            Character characterInst = foundCharacter.get();
            IntRange enemyRange = getEnemyLevelsRange(enemyLevel, characterInst.getLevel());
            List<Enemy> enemies =  List.of(this.enemyService.createRandomEnemyBasedOnLevel(
                    RandomUtils.getRandomValueWithinRange(enemyRange.min(), enemyRange.max()), enemyType, this.getEnemyStatsMultipler(enemyType)
            ));

            FightReport report =  this.battleManagerService.performNormalFight(List.of(characterInst), enemies);

            characterInst.gainExperience(report.getGainedExperience());
            this.characterService.update(characterInst);
            return report;
        }
        return null;
    }

    @PostMapping("/debug/attack-many-characters")
    public FightReport DebugAttackMultipleCharacters() throws Exception {
        List<Character> foundCharacters = this.characterService.findUserCharacters(this.authenticationFacade.getJwtTokenPayload().id());
        int randomLevel = RandomUtils.getRandomValueWithinRange(1,100);
        List<Enemy> enemies =  List.of(this.enemyService.createRandomEnemyBasedOnLevel(
                randomLevel,Enemy.EnemyType.NORMAL, 2));

        if(!foundCharacters.isEmpty()){
            return this.battleManagerService.performNormalFight(foundCharacters, enemies);
        }
        return null;
    }
    @PostMapping("/debug/many-vs-many")
    public FightReport DebugManyVsMany() throws Exception {
        List<Character> foundCharacters = this.characterService.findUserCharacters(this.authenticationFacade.getJwtTokenPayload().id());
        List<Enemy> enemies =  List.of(
            this.enemyService.createRandomEnemyBasedOnLevel(20,Enemy.EnemyType.IMPOSSIBLE,2),
            this.enemyService.createRandomEnemyBasedOnLevel(25,Enemy.EnemyType.NORMAL, 1.2),
            this.enemyService.createRandomEnemyBasedOnLevel(25,Enemy.EnemyType.EASY, 0.8)
        );
        if(!foundCharacters.isEmpty()){
            return this.battleManagerService.performNormalFight(foundCharacters, enemies);
        }
        return null;
    }

}
