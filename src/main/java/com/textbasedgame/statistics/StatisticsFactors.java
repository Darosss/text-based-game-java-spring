package com.textbasedgame.statistics;

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

        //BLOCK
        public static final double STRENGTH_BLOCK = 0.2;
        public static final double DEXTERITY_BLOCK = 0.05;

        //DODGE
        public static final double DEXTERITY_DODGE = 0.3;
        public static final double LUCK_DODGE= 0.1;

        //DOUBLE ATTACK
        public static final double DEXTERITY_DOUBLE_ATTACK = 0.15;

        //PARRYING
        public static final double DEXTERITY_PARRYING = 0.20;
        public static final double STRENGTH_PARRYING = 0.05;
        public static final double LUCK_PARRYING = 0.03;

        //CRITIC
        public static final double DEXTERITY_CRITIC = 0.15;
        public static final double LUCK_CRITIC = 0.1;

        //LETHAL CRITIC
        public static final double DEXTERITY_LETHAL_CRITIC = 0.05;
        public static final double LUCK_LETHAL_CRITIC = 0.03;

        //THREAT
        public static final double STRENGTH_THREAT = 0.1;
        public static final double CHARISMA_THREAT= 0.4;

        //INITIATIVE
        public static final double DEXTERITY_INITIATIVE= 0.2;
        public static final double INTELLIGENCE_INITIATIVE = 0.2;
        public static final double CHARISMA_INITIATIVE = 0.05;




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

        public static int get_BLOCK_Factors(int strength, int dexterity) {
                double strengthFactor = strength * StatisticsFactors.STRENGTH_BLOCK;
                double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_BLOCK;
                return (int) (strengthFactor + dexterityFactor);
        }

        public static int get_DOUBLE_ATTACK_Factors(int dexterity) {
                double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_DOUBLE_ATTACK;
                return (int) (dexterityFactor);
        }
        public static int get_DODGE_Factors(int dexterity, int luck) {
                double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_DODGE;
                double luckFactor = luck * StatisticsFactors.LUCK_DODGE;
                return (int) (dexterityFactor + luckFactor);
        }
        public static int get_PARRYING_Factors(int strength, int dexterity, int luck) {
                double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_PARRYING;
                double strengthFactor = strength * StatisticsFactors.STRENGTH_PARRYING;
                double luckFactor = luck * StatisticsFactors.LUCK_PARRYING;
                return (int) (dexterityFactor + strengthFactor + luckFactor);
        }
        public static int get_CRITIC_Factors(int dexterity, int luck) {
                double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_CRITIC;
                double luckFactor = luck * StatisticsFactors.LUCK_CRITIC;
                return (int) (dexterityFactor + luckFactor);
        }
        public static int get_LETHAL_CRITIC_Factors(int dexterity, int luck) {
                double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_LETHAL_CRITIC;
                double luckFactor = luck * StatisticsFactors.LUCK_LETHAL_CRITIC;
                return (int) (dexterityFactor + luckFactor);
        }
        public static int get_THREAT_Factors(int strength, int charisma) {
                double strengthFactor = strength * StatisticsFactors.STRENGTH_THREAT;
                double charismaFactor = charisma * StatisticsFactors.CHARISMA_THREAT;
                return (int) (strengthFactor + charismaFactor);
        }

        public static int get_INITIATIVE_Factors(int dexterity, int intelligence, int charisma){
            double dexterityFactor = dexterity * StatisticsFactors.DEXTERITY_INITIATIVE;
            double intelligenceFactor = intelligence * StatisticsFactors.INTELLIGENCE_INITIATIVE;
            double charismaFactor = charisma * StatisticsFactors.CHARISMA_INITIATIVE;

            return (int) (dexterityFactor + intelligenceFactor + charismaFactor);
        }
}
