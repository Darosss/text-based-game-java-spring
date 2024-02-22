package com.example.battle;

import com.example.battle.reports.FightReport;
import com.example.characters.Character;
import com.example.enemies.Enemy;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BattleManagerService {
    //TODO: add reports into database
    public FightReport performNormalFight(List<Character> characters, List<Enemy> enemies, int mainHeroLevel) {
        Fight fightInstance = new Fight(characters, enemies, mainHeroLevel);


        //TODO: here we will need to checks whether fight is win - give xp, gold etc
        // lose = do nothing? later some xp +- 5% from whole
        // draw = do nothing later some xp +- 15% from whole
        //TODO: later - achievements / statistics  for user add
        fightInstance.startFight();
        return fightInstance.getFightReport();
    }


    public FightReport performFight(Character character, Enemy enemy, int mainHeroLevel){
        Fight fightInstance = new Fight(List.of(character), List.of(enemy), mainHeroLevel);
        fightInstance.startFight();
        return fightInstance.getFightReport();
    }
    public FightReport performTeamFight(List<Character> characters, List<Enemy> enemies, int mainHeroLevel){
        Fight fightInstance = new Fight(characters, enemies, mainHeroLevel);
        fightInstance.startFight();
        return fightInstance.getFightReport();
    }
}
