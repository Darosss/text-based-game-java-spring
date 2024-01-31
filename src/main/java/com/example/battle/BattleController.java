package com.example.battle;

import com.example.auth.AuthenticationFacade;
import com.example.auth.SecuredRestController;
import com.example.characters.Character;
import com.example.characters.CharacterService;
import com.example.enemies.Enemy;
import com.example.enemies.EnemyService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/debug/attack")
    public Fight.FightReport DebugAttack() throws Exception {
        Optional<Character> foundCharacter = this.characterService.findOneMainCharacterByUserId(this.authenticationFacade.getJwtTokenPayload().id());
        List<Enemy> enemies =  List.of(this.enemyService.createRandomEnemy());
        if(foundCharacter.isPresent()){
            return this.battleManagerService.performNormalFight(List.of(foundCharacter.get()), enemies);
        }
       return null;
    }

    @PostMapping("/debug/attack-many-characters")
    public Fight.FightReport DebugAttackMultipleCharacters() throws Exception {
        List<Character> foundCharacters = this.characterService.findUserCharacters(this.authenticationFacade.getJwtTokenPayload().id());
        List<Enemy> enemies =  List.of(this.enemyService.createRandomEnemy());
        if(!foundCharacters.isEmpty()){
            return this.battleManagerService.performNormalFight(foundCharacters, enemies);

        }
        return null;
    }

}
