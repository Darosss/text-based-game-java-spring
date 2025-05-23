package com.textbasedgame.battle;

import com.textbasedgame.battle.reports.FightReport;
import com.textbasedgame.characters.Character;
import com.textbasedgame.enemies.Enemy;
import com.textbasedgame.users.User;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BattleManagerService {
    //TODO: add reports into database
    public FightReport performNormalFight(User user, List<Character> characters, List<Enemy> enemies, int mainHeroLevel) {
        Fight fightInstance = new Fight(user, characters, enemies, mainHeroLevel, false);


        //TODO: here we will need to checks whether fight is win - give xp, gold etc
        // lose = do nothing? later some xp +- 5% from whole
        // draw = do nothing later some xp +- 15% from whole
        //TODO: later - achievements / statistics  for user add
        fightInstance.startFight();
        return fightInstance.getFightReport();
    }


    public FightReport performFight(User user, Character character, Enemy enemy, int mainHeroLevel){
        Fight fightInstance = new Fight(user, List.of(character), List.of(enemy), 50, mainHeroLevel, false);
        fightInstance.startFight();
        return fightInstance.getFightReport();
    }
    public FightReport performTeamFight(User user, List<Character> characters, List<Enemy> enemies, int mainHeroLevel){
        Fight fightInstance = new Fight(user, characters, enemies, 300, mainHeroLevel, true);
        fightInstance.startFight();
        return fightInstance.getFightReport();
    }
}
