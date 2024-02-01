package com.example.battle;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.battle.reports.FightReport;
import com.example.characters.Character;
import com.example.characters.CharacterService;
import com.example.enemies.Enemy;
import com.example.enemies.EnemyService;
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


    @PostMapping("/debug/attack/{enemyType}")
    public FightReport DebugAttack(
            @PathVariable Enemy.EnemyType enemyType
            ) throws Exception {
        Optional<Character> foundCharacter = this.characterService.findOneMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());
        List<Enemy> enemies =  List.of(this.enemyService.createRandomEnemy(enemyType));
        if(foundCharacter.isPresent()){
            Character characterInst = foundCharacter.get();
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
        List<Enemy> enemies =  List.of(this.enemyService.createRandomEnemy(Enemy.EnemyType.NORMAL));
        if(!foundCharacters.isEmpty()){
            return this.battleManagerService.performNormalFight(foundCharacters, enemies);

        }
        return null;
    }
    @PostMapping("/debug/many-vs-many")
    public FightReport DebugManyVsMany() throws Exception {
        List<Character> foundCharacters = this.characterService.findUserCharacters(this.authenticationFacade.getJwtTokenPayload().id());
        List<Enemy> enemies =  List.of(this.enemyService.createRandomEnemy(Enemy.EnemyType.NORMAL), this.enemyService.createRandomEnemy(Enemy.EnemyType.NORMAL), this.enemyService.createRandomEnemy(Enemy.EnemyType.NORMAL));
        if(!foundCharacters.isEmpty()){
            return this.battleManagerService.performNormalFight(foundCharacters, enemies);
        }
        return null;
    }

}
