package com.example.statistics;

import dev.morphia.annotations.ExternalEntity;
import org.slf4j.LoggerFactory;

import java.util.Map;
import org.slf4j.Logger;

@ExternalEntity(target= HeroStatisticsObject.class)
public class HeroStatisticsObject {
    private static final Logger logger = LoggerFactory.getLogger(HeroStatisticsObject.class);
    private final Map<BaseStatisticsNamesEnum, BaseStatisticObject> statistics =
            StatisticsUtils.generateDefaultHeroStatistics();
    private final Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> additionalStatistics =
            StatisticsUtils.generateDefaultHeroAdditionalStatistics();

    public HeroStatisticsObject(){
        statistics.forEach((baseStat, statVal)->{
            this.updateAdditionalStatisticsBasedOnBasic(baseStat);
        });
    }

    public int getMaxHealthEffValue(){
        return this.additionalStatistics.get(AdditionalStatisticsNamesEnum.MAX_HEALTH).getEffectiveValue();

    }
    public void updateStatistic(BaseStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.statistics.get(statName).updateStatistic(value, updateType);
        this.updateAdditionalStatisticsBasedOnBasic(statName);
    }
    public void increaseStatistic(BaseStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.statistics.get(statName).increaseStatistic(value, updateType);
        this.updateAdditionalStatisticsBasedOnBasic(statName);
    }
    public void updateStatisticsOnEquip(BaseStatisticsNamesEnum statName, int value, float percentageValue) {
        this.statistics.get(statName).increaseStatistic(value, StatisticsUtils.StatisticUpdateType.BONUS);
        this.statistics.get(statName).increaseStatistic(((int)percentageValue), StatisticsUtils.StatisticUpdateType.PERCENTAGE_BONUS);
        this.updateAdditionalStatisticsBasedOnBasic(statName);
    }
    public void updateStatisticsOnUnEquip(BaseStatisticsNamesEnum statName, int value, float percentageValue) {
        this.statistics.get(statName).decreaseStatistic(value, StatisticsUtils.StatisticUpdateType.BONUS);
        this.statistics.get(statName).decreaseStatistic(((int)percentageValue), StatisticsUtils.StatisticUpdateType.PERCENTAGE_BONUS);
        this.updateAdditionalStatisticsBasedOnBasic(statName);
    }

    public void updateAdditionalStatisticsOnEquip(AdditionalStatisticsNamesEnum statName, int value, float percentageValue) {
        this.additionalStatistics.get(statName).increaseStatistic(value, StatisticsUtils.StatisticUpdateType.BONUS);
        this.additionalStatistics.get(statName).increaseStatistic(((int)percentageValue), StatisticsUtils.StatisticUpdateType.PERCENTAGE_BONUS);
    }
    public void updateAdditionalStatisticsOnUnEquip(AdditionalStatisticsNamesEnum statName, int value, float percentageValue) {
        this.additionalStatistics.get(statName).decreaseStatistic(value, StatisticsUtils.StatisticUpdateType.BONUS);
        this.additionalStatistics.get(statName).decreaseStatistic(((int)percentageValue), StatisticsUtils.StatisticUpdateType.PERCENTAGE_BONUS);
    }


    public void updateAdditionalStatistic(AdditionalStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.additionalStatistics.get(statName).updateStatistic(value, updateType);
    }

    public void increaseAdditionalStatistic(AdditionalStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.additionalStatistics.get(statName).increaseStatistic(value, updateType);
    }

    public Map<BaseStatisticsNamesEnum, BaseStatisticObject> getStatistics() {
        return statistics;
    }

    public Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> getAdditionalStatistics() {
        return additionalStatistics;
    }

    protected int getEffectiveValueByStatName(BaseStatisticsNamesEnum name) {
        return this.statistics.get(name).getValue();
    }

    private void setBaseStatBonus(AdditionalStatisticsNamesEnum name, int value) {
     this.additionalStatistics.get(name).setBaseStatBonus(value);
    }

    private void updateStrengthStatisticsBasedOnBasicStat(){
        int strengthEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.STRENGTH);
        int constitutionEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.CONSTITUTION);
        int dexterityEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.DEXTERITY);
        int luckEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.LUCK);
        int charismaEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.CHARISMA);

        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.ARMOR,
                StatisticsFactors.get_ARMOR_Factors(strengthEff, constitutionEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.MAX_HEALTH,
                StatisticsFactors.get_MAX_HEALTH_Factors(strengthEff, constitutionEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.MIN_DAMAGE,
                StatisticsFactors.get_MIN_DAMAGE_Factors(strengthEff, dexterityEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.MAX_DAMAGE,
                StatisticsFactors.get_MAX_DAMAGE_Factors(strengthEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.BLOCK,
                StatisticsFactors.get_BLOCK_Factors(strengthEff, dexterityEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.PARRYING,
                StatisticsFactors.get_PARRYING_Factors(strengthEff, dexterityEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.THREAT,
                StatisticsFactors.get_THREAT_Factors(strengthEff, charismaEff)
        );

    }

    private void updateConstitutionStatisticsBasedOnBasicStat() {
        int strengthEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.STRENGTH);
        int constitutionEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.CONSTITUTION);

        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.ARMOR,
                StatisticsFactors.get_ARMOR_Factors(strengthEff, constitutionEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.MAX_HEALTH,
                StatisticsFactors.get_MAX_HEALTH_Factors(strengthEff, constitutionEff)
        );
    }
    private void updateDexterityStatisticsBasedOnBasicStat() {
        int strengthEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.STRENGTH);
        int dexterityEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.DEXTERITY);
        int luckEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.LUCK);
        int intelligenceEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.INTELLIGENCE);
        int charismaEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.CHARISMA);

        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.MIN_DAMAGE,
                StatisticsFactors.get_MIN_DAMAGE_Factors(strengthEff, dexterityEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.DOUBLE_ATTACK,
                StatisticsFactors.get_DOUBLE_ATTACK_Factors(dexterityEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.DODGE,
                StatisticsFactors.get_DODGE_Factors(dexterityEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.PARRYING,
                StatisticsFactors.get_PARRYING_Factors(dexterityEff, strengthEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.CRITIC,
                StatisticsFactors.get_CRITIC_Factors(dexterityEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.LETHAL_CRITIC,
                StatisticsFactors.get_LETHAL_CRITIC_Factors(dexterityEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.BLOCK,
                StatisticsFactors.get_BLOCK_Factors(strengthEff, dexterityEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.INITIATIVE,
                StatisticsFactors.get_INITIATIVE_Factors(dexterityEff, charismaEff, intelligenceEff)
        );
    }

    private void updateIntelligenceStatisticsBasedOnBasicStat() {
        int intelligenceEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.INTELLIGENCE);
        int charismaEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.CHARISMA);
        int dexterityEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.DEXTERITY);

        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.INITIATIVE,
                StatisticsFactors.get_INITIATIVE_Factors(dexterityEff, charismaEff, intelligenceEff)
        );

    }
    private void updateLuckStatisticsBasedOnBasicStat() {
        int strengthEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.STRENGTH);
        int dexterityEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.DEXTERITY);
        int luckEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.LUCK);

        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.PARRYING,
                StatisticsFactors.get_PARRYING_Factors(dexterityEff, strengthEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.CRITIC,
                StatisticsFactors.get_CRITIC_Factors(dexterityEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.LETHAL_CRITIC,
                StatisticsFactors.get_LETHAL_CRITIC_Factors(dexterityEff, luckEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.DODGE,
                StatisticsFactors.get_DODGE_Factors(dexterityEff, luckEff)
        );

    }
    protected void updateCharismaStatisticsBasedOnBasicStat() {
        int strengthEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.STRENGTH);
        int charismaEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.CHARISMA);
        int dexterityEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.DEXTERITY);
        int intelligenceEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.INTELLIGENCE);

        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.THREAT,
                StatisticsFactors.get_THREAT_Factors(strengthEff, charismaEff)
        );
        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.INITIATIVE,
                StatisticsFactors.get_INITIATIVE_Factors(dexterityEff, charismaEff, intelligenceEff)
        );

    }
    private void updateAdditionalStatisticsBasedOnBasic(BaseStatisticsNamesEnum statName){
        System.out.println("updateAdditionalStatisticsBasedOnBasic CALL");
        switch (statName){
            case STRENGTH:{
                //min, max dmg, armor (or toughness), critical damage / dead damage (something like cbk)
                this.updateStrengthStatisticsBasedOnBasicStat();

                logger.debug("UPDATED STRENGTH");

                break;
            }
            case DEXTERITY:{
                this.updateDexterityStatisticsBasedOnBasicStat();
                logger.debug("UPDATED DEXTERITY");

                // crit chance max 50,
                // base should be 5 maybe
                // increases chances for critic, hit chance, slightly add chance to dodge enemy attacks(max50)
                // increase initiative
                break;
            }
            case CONSTITUTION:{
                this.updateConstitutionStatisticsBasedOnBasicStat();
                logger.debug("UPDATED CONSTITUTION");
                // adds max health, maybe if provided: regeneration health per hour, toughness / endurance
                // add basic amor? block chance
                // more resistance for de buffs later
                break;
            }
            case INTELLIGENCE:{
                this.updateIntelligenceStatisticsBasedOnBasicStat();
                logger.debug("UPDATED INTELLIGENCE");
                // if magic provided: more magic dmg, increase initiative,
                // more intelligence(but as basic only, bonus is not counted) will decrease max level of items that user can wear

                break;
            }
            case LUCK:{
                this.updateLuckStatisticsBasedOnBasicStat();
                logger.debug("UPDATED LUCK");
                // increase critical, deadly strike, maybe % to found loot from enemies
                break;
            }
            case CHARISMA:{
                this.updateCharismaStatisticsBasedOnBasicStat();
                logger.debug("UPDATED CHARISMA");
                // increase threat, small initiative bonus
                // more resistance for de buffs later
                break;
            }
        }

    }
}
