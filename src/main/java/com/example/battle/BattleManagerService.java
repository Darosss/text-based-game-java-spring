package com.example.battle;

import com.example.battle.reports.FightTurnReport;
import com.example.characters.BaseHero;
import com.example.characters.Character;
import com.example.enemies.Enemy;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class BattleManagerService {
    public List<FightTurnReport> performNormalFight(Map<ObjectId, Character> characters, Map<ObjectId, Enemy> enemies) {
        Fight fightInstance = new Fight(characters, enemies);

        fightInstance.startFight();
        return fightInstance.getFightReport();
    }



}
