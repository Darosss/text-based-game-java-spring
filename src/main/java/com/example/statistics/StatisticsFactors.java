package com.example.statistics;

public class StatisticsFactors
{
        //ARMOR
        public static final double STRENGTH_ARMOR = 0.2;
        public static final double CONSTITUTION_ARMOR = 0.1;

        //MAX HEALTH
        public static final double STRENGTH_MAX_HEALTH = 1.1;
        public static final double CONSTITUTION_MAX_HEALTH = 5;

        //MIN DMG
        public static final double STRENGTH_MIN_DMG = 0.2;
        public static final double DEXTERITY_MIN_DMG = 0.02;

        //MAX DMG
        public static final double STRENGTH_MAX_DMG = 0.2;


        public static int get_ARMOR_Factors(int strength, int constitute) {
                double strengthFactor = strength * StatisticsFactors.STRENGTH_ARMOR;
                double constituteFactor = constitute * StatisticsFactors.CONSTITUTION_ARMOR;
                return (int) (strengthFactor + constituteFactor);
        }
        public static int get_MAX_HEALTH_Factors(int strength, int constitute) {
                double strengthFactor = strength * StatisticsFactors.STRENGTH_MAX_HEALTH;
                double constituteFactor = constitute * StatisticsFactors.CONSTITUTION_MAX_HEALTH;
                return (int) (strengthFactor + constituteFactor);
        }
        public static int get_MIN_DAMAGE_Factors(int strength, int dexterity) {
                double strengthFactor = strength * StatisticsFactors.STRENGTH_MIN_DMG;
                double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_MIN_DMG;
                return (int) (strengthFactor + dexterityFactor);
        }
        public static int get_MAX_DAMAGE_Factors(int strength) {
                double strengthFactor = strength * StatisticsFactors.STRENGTH_MAX_DMG;
                return (int) (strengthFactor);
        }
}
