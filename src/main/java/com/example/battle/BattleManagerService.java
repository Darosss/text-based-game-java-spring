package com.example.battle;

import com.example.characters.Character;
import com.example.enemies.Enemy;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BattleManagerService {
    public Fight.FightReport performNormalFight(List<Character> characters, List<Enemy> enemies) {
        Fight fightInstance = new Fight(characters, enemies);


        //TODO: here we will need to checks whether fight is win - give xp, gold etc
        // lose = do nothing?
        // draw = do nothing
        //TODO: later - achievements / statistics  for user add
        fightInstance.startFight();
        return fightInstance.getFightReport();
    }



}
