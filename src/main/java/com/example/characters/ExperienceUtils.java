package com.example.characters;

import com.example.enemies.Enemy;

public class ExperienceUtils {
    public static long BASE_EXPERIENCE_FOR_LEVEL = 100L;
    public static double EXPERIENCE_LEVEL_SCALING_FACTOR = 30.0;
    public static double EXPERIENCE_LEVEL_FACTOR_BASE = 0.9;
    public static double EXPERIENCE_LEVEL_FACTOR_EXPONENT = 1.1;

    public static final int ENEMY_DEFEAT_BASE_EXPERIENCE = 25;
    public static final double ENEMY_DEFEAT_LEVEL_FACTOR_BASE = 1.2;
    public static final double ENEMY_DEFEAT_LEVEL_FACTOR_EXPONENT = 0.3;

    public static long calculateExperienceFromEnemy(int playerLevel, int enemyLevel, Enemy.EnemyType enemyType) {
        int baseExperience = ENEMY_DEFEAT_BASE_EXPERIENCE;
        double levelDifference = enemyLevel - playerLevel;
        double enemyDifferenceFactor = levelDifference >= 0 ? Math.max(1, levelDifference + (0.2 * levelDifference) ) :
                Math.min(-1, (enemyLevel - levelDifference)  * baseExperience );

        double scalingFactor =
                baseExperience * (Math.pow(
                        enemyLevel+(ENEMY_DEFEAT_LEVEL_FACTOR_BASE*enemyLevel),
                        ENEMY_DEFEAT_LEVEL_FACTOR_EXPONENT))
                        +  (baseExperience * enemyDifferenceFactor);

        double enemyTypeBonus = getEnemyTypeBonus(enemyType);
        long experience = (long) Math.max(1, (baseExperience * scalingFactor * enemyTypeBonus));

        System.out.println("Scaling factor: "+scalingFactor+" | experience: " + experience +
                " | enemy level: " + enemyLevel+ " | player lvl: " + playerLevel + " | diff "
                + enemyDifferenceFactor +" b + d: " + baseExperience  * enemyDifferenceFactor );

        return experience;
    }

    public static long calculateExpToNextLevel(int currentLevel) {
        double levelScalingFactor = currentLevel/ EXPERIENCE_LEVEL_SCALING_FACTOR;
        double levelFactor = Math.pow(
                currentLevel+(EXPERIENCE_LEVEL_FACTOR_BASE*currentLevel),
                (EXPERIENCE_LEVEL_FACTOR_EXPONENT + levelScalingFactor)
        );
        long neededExp = (long) (BASE_EXPERIENCE_FOR_LEVEL * levelFactor);
        System.out.println("NEEDED " + neededExp);

        return neededExp;
    }
    private static double getEnemyTypeBonus(Enemy.EnemyType enemyType) {
        return switch (enemyType) {
            case EASY -> 0.5;
            case NORMAL -> 1.0;
            case MEDIUM -> 1.2;
            case HARD -> 1.5;
            case IMPOSSIBLE -> 2.0;
        };
    }
}
