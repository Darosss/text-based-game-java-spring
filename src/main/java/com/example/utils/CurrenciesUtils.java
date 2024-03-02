package com.example.utils;

import com.example.enemies.EnemyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrenciesUtils {
    private static final Logger logger = LoggerFactory.getLogger(CurrenciesUtils.class);
    public static long BASE_GOLD_FOR_ENEMY_LEVEL = 33L;
    public static final double ENEMY_IS_ALIVE_DEFEAT_GOLD_ADJUST = -0.3;
    private CurrenciesUtils(){}


    public static long calculateGoldFromEnemy(int enemyLevel, EnemyType enemyType, boolean isAlive) {
        double enemyTypeBonus = getEnemyTypeBonus(enemyType);

        long gold = (long) Math.max(1, (enemyLevel * BASE_GOLD_FOR_ENEMY_LEVEL) * enemyTypeBonus);

        logger.debug("Enemy level: {}, enemyType: {} -> {}, isAlive: {}, gold: {}, goldIsAlive: {}:",
                enemyLevel, enemyType, enemyTypeBonus, isAlive, gold, (int) Math.max(1, (gold + (gold * (ENEMY_IS_ALIVE_DEFEAT_GOLD_ADJUST)))) );
        return !isAlive ?
                gold :
                (int) Math.max(1, (gold + (gold * (ENEMY_IS_ALIVE_DEFEAT_GOLD_ADJUST))));
    }



    private static double getEnemyTypeBonus(EnemyType enemyType) {
        return switch (enemyType) {
            case COMMON -> 1.0;
            case UNCOMMON -> 1.5;
            case RARE -> 2.0;
            case EPIC -> 2.5;
            case BOSS -> 3.0;
            case ANCESTOR -> 5.0;
        };
    }
}
