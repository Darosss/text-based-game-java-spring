package com.example.characters;

import com.example.enemies.EnemyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExperienceUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExperienceUtils.class);
    public static long BASE_EXPERIENCE_FOR_LEVEL = 100L;
    public static double EXPERIENCE_LEVEL_SCALING_FACTOR = 30.0;
    public static double EXPERIENCE_LEVEL_FACTOR_BASE = 0.9;
    public static double EXPERIENCE_LEVEL_FACTOR_EXPONENT = 1.1;

    public static final int ENEMY_DEFEAT_BASE_EXPERIENCE = 50;
    public static final double ENEMY_DEFEAT_LEVEL_FACTOR_BASE = 1.2;
    public static final double ENEMY_DEFEAT_LEVEL_FACTOR_EXPONENT = 0.3;

    public static final double ENEMY_IS_ALIVE_DEFEAT_EXPERIENCE_ADJUST = -0.3;

    public static long calculateExperienceFromEnemy(int playerLevel, int enemyLevel, EnemyType enemyType, boolean isAlive) {
        int baseExperience = ENEMY_DEFEAT_BASE_EXPERIENCE;
        int levelDifference = enemyLevel - playerLevel;
        double enemyDifferenceFactor = levelDifference >= 0 ? Math.max(1, levelDifference + (0.2 * levelDifference) ) :( 1.0 + ( (double) levelDifference / 5.0) );

        double enemyTypeBonus = getEnemyTypeBonus(enemyType);


        double bonusExpForDifferenceLevel = (baseExperience * enemyDifferenceFactor) + (enemyTypeBonus * baseExperience / 2);
        double scalingFactor = Math.max(1,Math.pow(enemyLevel+(ENEMY_DEFEAT_LEVEL_FACTOR_BASE*enemyLevel),ENEMY_DEFEAT_LEVEL_FACTOR_EXPONENT));

        long experience = (long) Math.max(1, ((baseExperience * scalingFactor * enemyTypeBonus) + bonusExpForDifferenceLevel));

        logger.debug("Scaling factor: {}, experience: {}, enemyLevel: {}, playerLevel: {}, enemyDifferenceFactor: {}, bonusExpForDifferenceLevel: {} levelDifference: {}",
                scalingFactor, experience, enemyLevel, playerLevel, enemyDifferenceFactor, bonusExpForDifferenceLevel, levelDifference);
        return !isAlive ?
                experience :
                (int) Math.max(1, (experience + (experience * (ENEMY_IS_ALIVE_DEFEAT_EXPERIENCE_ADJUST))));
    }

    public static long calculateExpToNextLevel(int currentLevel) {
        double levelScalingFactor = currentLevel/ EXPERIENCE_LEVEL_SCALING_FACTOR;
        double levelFactor = Math.pow(
                currentLevel+(EXPERIENCE_LEVEL_FACTOR_BASE*currentLevel),
                (EXPERIENCE_LEVEL_FACTOR_EXPONENT + levelScalingFactor)
        );
        long neededExp = (long) (BASE_EXPERIENCE_FOR_LEVEL * levelFactor);
        logger.debug("Needed: {} exp for {} level ", neededExp, currentLevel);

        return neededExp;
    }
    private static double getEnemyTypeBonus(EnemyType enemyType) {
        return switch (enemyType) {
            case COMMON -> 0.5;
            case UNCOMMON -> 1.0;
            case RARE -> 1.2;
            case EPIC -> 1.7;
            case BOSS -> 2.5;
            case ANCESTOR -> 5.0;
        };
    }
}
