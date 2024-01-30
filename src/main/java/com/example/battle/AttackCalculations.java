package com.example.battle;

import com.example.battle.data.AttackBase;
import com.example.battle.data.AttackDebuffs;
import com.example.battle.data.AttackReturnData;
import com.example.characters.BaseHero;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.utils.RandomUtils;

import javax.annotation.Nullable;

public class AttackCalculations {

    private static AttackBase.AttackStrength getCalculatedAttackStrength(BaseHero hero){
        int lethalChance = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.LETHAL_CRITIC);
        int criticChance = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.CRITIC);

        if(RandomUtils.checkPercentageChance(lethalChance)) return AttackBase.AttackStrength.LETHAL;
        if(RandomUtils.checkPercentageChance(criticChance)) return AttackBase.AttackStrength.CRITIC;
        else return AttackBase.AttackStrength.NORMAL;

    }

    private static int getCalculatedAttackValue(BaseHero hero) {
        int minDmg = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.MIN_DAMAGE);
        int maxDmg = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.MIN_DAMAGE);
        return  RandomUtils.getRandomValueWithinRange(minDmg, maxDmg);
    }

    private static boolean isDoubleAttack(BaseHero hero) {
        int doubleAttackChance = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.DOUBLE_ATTACK);
        return RandomUtils.checkPercentageChance(doubleAttackChance);
    }

    private static AttackBase getAttackBaseValues(BaseHero hero, boolean asDoubledAttack) {
        int attackValue = getCalculatedAttackValue(hero);
        AttackBase.AttackStrength attackStrength = getCalculatedAttackStrength(hero);
        boolean isDoubleAttack = !asDoubledAttack && isDoubleAttack(hero);
        return new AttackBase(attackValue, attackStrength, isDoubleAttack);
    }

    //TODO: add debuffs
    @Nullable
    private static AttackDebuffs getAttackDebuffs() {
        return null;
    }

    private int getAndCalculateAdditionalAttackValues() {
        return 0;
    }

    public static AttackReturnData generateAttackValue(BaseHero hero, boolean asDoubledAttack/*TODO: add some optionals from fight?*/){
        AttackBase attackBase = getAttackBaseValues(hero, asDoubledAttack);
        AttackDebuffs attackDebuffs = getAttackDebuffs();
        return new AttackReturnData(hero.getName(), attackBase, attackDebuffs);
    }



}
