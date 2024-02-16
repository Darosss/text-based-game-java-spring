package com.example.items;

import com.example.statistics.AdditionalStatisticsNamesEnum;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;

//TODO: make stats helpers like weaponStats, rangedStats, armor stats etc.
public enum  ItemsSubtypes {
    //WEAPON_MELEE
    SWORD(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 1.0,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.1,
                    AdditionalStatisticsNamesEnum.PARRYING, 0.2
            )),Pair.of(2.0f, 4.0f)),
    MACE(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.2,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.1
            )), Pair.of(2.0f, 3.5f)),
    DAGGER(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.2,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 0.6,
                    AdditionalStatisticsNamesEnum.CRITIC, 0.2,
                    AdditionalStatisticsNamesEnum.LETHAL_CRITIC, 0.05,
                    AdditionalStatisticsNamesEnum.DODGE, 0.2,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.3
            )), Pair.of(0.5f, 1.2f)),
    HATCHET(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.6,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 0.9
            )), Pair.of(0.6f, 1.3f)),
    CLUB(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.6,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.1
            )), Pair.of(0.3f, 1.0f)),
    SPEAR(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.9,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.2
            )), Pair.of(1.0f, 2.5f)),

    WHIP(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.1,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.1,
                    AdditionalStatisticsNamesEnum.CRITIC, 0.1,
                    AdditionalStatisticsNamesEnum.LETHAL_CRITIC, 0.05,
                    AdditionalStatisticsNamesEnum.DODGE, 0.2,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.3
            )), Pair.of(0.3f, 0.9f)),
    SCIMITAR(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.7,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.2,
                    AdditionalStatisticsNamesEnum.PARRYING, 0.2
            )), Pair.of(0.9f, 1.3f)),
    //WEAPON_MELEE_TWO_HAND
    SWORD_TWO_HAND(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.7,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.9
            )), Pair.of(2.5f, 5.0f)),
    AXE(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.7,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.9
            )), Pair.of(2.6f, 5.1f)),
    KATANA(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 1.1,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.1
            )), Pair.of(1.0f, 1.5f)),
    HALBERD(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 1.2,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.5
            )), Pair.of(1.5f, 3.5f)),
    SLEDGEHAMMER(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 1.5,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 2.0
            )), Pair.of(2.0f, 4.0f)),
    //WEAPON_RANGED
    BOW (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.9,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.0,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.5
            )), Pair.of(0.8f, 1.2f)),
    CROSSBOW (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 1.5,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 2.0,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.3
            )), Pair.of(1.3f, 2.1f)),
    RANGED_HATCHET (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.3,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.2,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.5
            )), Pair.of(0.6f, 0.9f)),
    RANGED_DAGGER (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.5,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 0.5,
                    AdditionalStatisticsNamesEnum.CRITIC, 0.1,
                    AdditionalStatisticsNamesEnum.LETHAL_CRITIC, 0.05
            )), Pair.of(0.5f, 0.9f)),
    SLINGSHOT (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.MIN_DAMAGE, 0.2,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, 1.0,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.5
            )), Pair.of(0.2f, 0.5f)),

    //SHIELD
    WOODEN_SHIELD (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 2.0,
                    AdditionalStatisticsNamesEnum.BLOCK, 0.2
            )),Pair.of(1.5f, 1.7f)),
    BUCKLER (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 3.0,
                    AdditionalStatisticsNamesEnum.BLOCK, 0.3
            )), Pair.of(0.9f, 1.3f)),
    TARGE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 5.0,
                    AdditionalStatisticsNamesEnum.BLOCK, 0.5
            )),Pair.of(1.1f, 1.9f)),
    RONDACHE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 6.2,
                    AdditionalStatisticsNamesEnum.BLOCK, 1.0
            )), Pair.of(1.8f, 2.9f)),
    KITE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 7.5,
                    AdditionalStatisticsNamesEnum.BLOCK, 1.2
            )), Pair.of(2.8f, 4.0f)),
    HEATER (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 9.5,
                    AdditionalStatisticsNamesEnum.BLOCK, 1.5
            )), Pair.of(2.3f, 3.0f)),
    PAVISE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 10.0,
                    AdditionalStatisticsNamesEnum.BLOCK, 1.8
            )), Pair.of(2.9f, 4.1f)),
    //CHEST_ARMOR
    GAMBESON (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 8.0,
                    AdditionalStatisticsNamesEnum.DODGE, 1.0,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 1.0

            )), Pair.of(2.5f, 3.1f)),
    LEATHER_ARMOR (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 9.0,
                    AdditionalStatisticsNamesEnum.DODGE, 0.5,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.5
            )), Pair.of(8.0f, 14.0f)),
    SHELL (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 11.0,
                    AdditionalStatisticsNamesEnum.DODGE, 0.2,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.2
            )), Pair.of(14.0f, 18.0f)),
    SCALE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 12.0
            )), Pair.of(14.0f,18.0f)),
    LAMELLAR (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 13.0,
                    AdditionalStatisticsNamesEnum.DODGE, -0.1,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.1
            )), Pair.of(16.0f, 20.0f)),
    LAMINAR (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 14.0,
                    AdditionalStatisticsNamesEnum.DODGE, -0.1,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.1
            )), Pair.of(16.0f, 20.0f)),
    CHAINMAIL(new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 18.0,
                    AdditionalStatisticsNamesEnum.DODGE, -0.3,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.3
            )), Pair.of(16.0f, 20.0f)),
    PLATED_MAIL (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 15.0,
                    AdditionalStatisticsNamesEnum.DODGE, -0.4,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.4
            )), Pair.of(18.0f, 20.0f)),

    BRIGANDINE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 20.0,
                    AdditionalStatisticsNamesEnum.DODGE, -0.5,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.5
            )), Pair.of(14.0f, 18.0f)),
    PLATE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 25.0,
                    AdditionalStatisticsNamesEnum.DODGE, -1.0,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -1.0
            )), Pair.of(19.0f, 23.0f)),

    //HELMET
    HAT (new HashMap<>(
            Map.of(AdditionalStatisticsNamesEnum.INITIATIVE, 1.0
            )), Pair.of(0.2f, 0.5f)),
    LEATHER_HELMET (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 0.7,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.3
            )), Pair.of(1.2f, 1.5f)),
    KETTLE (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.0,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.1
            )), Pair.of(1.7f, 2.5f)),
    SPANGENHELM (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.5,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -0.1
            )), Pair.of(1.9f, 2.5f)),
    HOUNSKULL (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.9,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -1.0
            )), Pair.of(2.1f, 2.5f)),
    ARMET (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 2.5,
                    AdditionalStatisticsNamesEnum.INITIATIVE, -1.0
            )), Pair.of(1.8f, 2.8f)),
    GREAT_HELM (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 3.5,
                    AdditionalStatisticsNamesEnum.MAX_DAMAGE, -1.0
            )), Pair.of(1.9f, 3.1f)),

    //GLOVES
    LEATHER_GLOVES (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 0.3
            )), Pair.of(0.9f, 1.2f)),
    IRON_GLOVES (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 0.9
            )), Pair.of(1.3f, 2.2f)),
    GAUNTLET (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.5
            )), Pair.of(0.9f, 1.5f)),
    STEEL_GLOVES (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 3.0
            )), Pair.of(2.2f, 3.0f)),
    //RING
    SIMPLE_RING(Pair.of(0.1f, 0.1f)),
    SIGNET(Pair.of(0.1f, 0.1f)),

    //NECKLACE
    CHAIN_NECKLACE(Pair.of(0.1f, 0.1f)),
    TORQUE(Pair.of(0.1f, 0.1f)),
    PEARLS_NECKLACE(Pair.of(0.1f, 0.1f)),
    LOCKET(Pair.of(0.1f, 0.1f)),

    //BOOTS
    LEATHER_BOOTS (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 0.8,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.5
            )), Pair.of(0.9f, 1.3f)),
    IRON_BOOTS (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.9
            )), Pair.of(2.0f, 2.3f)),
    STEEL_BOOTS (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 4.0
            )), Pair.of(1.9f, 2.5f)),

    //LEG_ARMOR
    LEATHER_LEG_ARMOR (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.2,
                    AdditionalStatisticsNamesEnum.INITIATIVE, 0.2
            )), Pair.of(0.9f, 1.3f)),
    IRON_LEG_ARMOR (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.2
            )), Pair.of(2.0f, 2.3f)),
    STEEL_LEG_ARMOR (new HashMap<>(
            Map.of( AdditionalStatisticsNamesEnum.ARMOR, 1.2
            )), Pair.of(1.9f, 2.5f)),

    //CONSUMABLE
    HEALTH_POTION(15.0, Pair.of(0.1f, 0.1f)),
    COMMON_FOOD(10.0, Pair.of(0.1f, 0.1f)),

    //NEUTRAL
    COMMON_NEUTRAL(Pair.of(0.1f, 0.3f)),


    //MERCENARY
    ASSASSIN(Pair.of(0.1f, 0.1f)),
    BATTLE_MASTER(Pair.of(0.1f, 0.1f)),
    MEDIC(Pair.of(0.1f, 0.1f)),
    BARD(Pair.of(0.1f, 0.1f)),
    STRATEGIST(Pair.of(0.1f, 0.1f)),
    DEFENDER(Pair.of(0.1f, 0.1f)),
    SCOUT(Pair.of(0.1f, 0.1f)),
    THUG(Pair.of(0.1f, 0.1f));
    private final Map<AdditionalStatisticsNamesEnum, Double> additionalStatisticsPerLevel;
    private final double healthGainPerLevel;
    private final Pair<Float, Float> weightRange;

    ItemsSubtypes(Map<AdditionalStatisticsNamesEnum, Double> additionalStatisticsPerLevel,
    Pair<Float, Float> weightRange
    ) {
        this.additionalStatisticsPerLevel = additionalStatisticsPerLevel;
        this.healthGainPerLevel = 0;
        this.weightRange = weightRange;
    }

    ItemsSubtypes(Pair<Float, Float> weightRange){
        this.additionalStatisticsPerLevel=new HashMap<>();
        this.healthGainPerLevel = 0;
        this.weightRange = weightRange;
    }



    /**
     * For consumable health gain
     * @param healthGain health gain per item level
     */
    ItemsSubtypes(double healthGain, Pair<Float, Float> weightRange){
        this.healthGainPerLevel = healthGain;
        this.additionalStatisticsPerLevel=new HashMap<>();
        this.weightRange = weightRange;
    }

    public double getHealthGainPerLevel() {
        return healthGainPerLevel;
    }

    public Map<AdditionalStatisticsNamesEnum, Double> getAdditionalStatisticsPerLevel() {
        return additionalStatisticsPerLevel;
    }

    public Pair<Float, Float> getWeightRange() {
        return weightRange;
    }

}
