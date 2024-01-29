package com.example.statistics;

import dev.morphia.annotations.ExternalEntity;

import java.util.Map;

@ExternalEntity(target= HeroStatisticsObject.class)
public class HeroStatisticsObject {

    private final Map<BaseStatisticsNamesEnum, BaseStatisticObject> statistics =
            StatisticsUtils.generateDefaultHeroStatistics();
    private final Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> additionalStatistics =
            StatisticsUtils.generateDefaultHeroAdditionalStatistics();

    public HeroStatisticsObject(){}
    public void updateStatistic(BaseStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.statistics.get(statName).updateStatistic(value, updateType);
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
    public void decreaseAdditionalStatistic(AdditionalStatisticsNamesEnum statName, int value, StatisticsUtils.StatisticUpdateType updateType) {
        this.additionalStatistics.get(statName).decreaseStatistic(value, updateType);
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

        this.setBaseStatBonus(AdditionalStatisticsNamesEnum.MIN_DAMAGE,
                StatisticsFactors.get_MIN_DAMAGE_Factors(strengthEff, dexterityEff)
        );
    }

    private void updateIntelligenceStatisticsBasedOnBasicStat() {
        int intelligenceEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.INTELLIGENCE);

    }
    private void updateLuckStatisticsBasedOnBasicStat() {
        int intelligenceEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.INTELLIGENCE);

    }
    protected void updateCharismaStatisticsBasedOnBasicStat() {
        int intelligenceEff = getEffectiveValueByStatName(BaseStatisticsNamesEnum.INTELLIGENCE);

    }
    private void updateAdditionalStatisticsBasedOnBasic(BaseStatisticsNamesEnum statName){
        switch (statName){
            case STRENGTH:{
                //min, max dmg, armor (or toughness), critical damage / dead damage (something like cbk)
                this.updateStrengthStatisticsBasedOnBasicStat();

                System.out.println("UPDATED STRENGTH");
                break;
            }
            case DEXTERITY:{
                this.updateDexterityStatisticsBasedOnBasicStat();
                System.out.println("UPDATED DEXTERITY");

                // crit chance max 50,
                // base should be 5 maybe
                // increases chances for critic, hit chance, slightly add chance to dodge enemy attacks(max50)
                // increase initiative
                break;
            }
            case CONSTITUTION:{
                this.updateConstitutionStatisticsBasedOnBasicStat();
                System.out.println("UPDATED CONSTITUTION");
                // adds max health, maybe if provided: regeneration health per hour, toughness / endurance
                // add basic amor? block chance
                // more resistance for de buffs later
                break;
            }
            case INTELLIGENCE:{
                this.updateIntelligenceStatisticsBasedOnBasicStat();
                System.out.println("UPDATED INTELLIGENCE");
                // if magic provided: more magic dmg, increase initiative,
                // more intelligence(but as basic only, bonus is not counted) will decrease max level of items that user can wear

                break;
            }
            case LUCK:{
                this.updateLuckStatisticsBasedOnBasicStat();
                System.out.println("UPDATED LUCK");
                // increase critical, deadly strike, maybe % to found loot from enemies
                break;
            }
            case CHARISMA:{
                this.updateCharismaStatisticsBasedOnBasicStat();
                System.out.println("UPDATED CHARISMA");
                // increase threat, small initiative bonus
                // more resistance for de buffs later
                break;
            }
        }

    }
}
