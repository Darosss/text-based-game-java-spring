package com.example.battle;

import com.example.battle.data.AttackReturnData;
import com.example.battle.data.DefendReturnData;
import com.example.characters.BaseHero;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.utils.RandomUtils;

import java.util.Optional;

public class DefendCalculations {


    private static int getCalculatedArmorAbsorption(BaseHero hero, int rawAttackValue) {
        int armor = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.ARMOR);

        return (int) (rawAttackValue * 0.5);
    }

    private static boolean isBlocked(BaseHero hero){
        int blockChance = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.BLOCK);
        return RandomUtils.checkPercentageChance(blockChance);
    }
    private static boolean isDodged(BaseHero hero){
        int dodgeChance = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.DODGE);
        return RandomUtils.checkPercentageChance(dodgeChance);
    }
    private static boolean isPaired(BaseHero hero){
        int parryChance = hero.getAdditionalStatEffective(AdditionalStatisticsNamesEnum.PARRYING);
        return RandomUtils.checkPercentageChance(parryChance);
    }

    private static DefendReturnData.DefendType getDefendType(BaseHero hero) {
        if(isDodged(hero)) return DefendReturnData.DefendType.DODGED;
        else if(isPaired(hero)) return DefendReturnData.DefendType.PAIRED;
        else if(isBlocked(hero)) return DefendReturnData.DefendType.BLOCKED;
        else return DefendReturnData.DefendType.NULL;
    }

    private static CombatReturnData executeParryAttack(BaseHero hero, BaseHero pairedHero) {
        //TODO: remove those logs later
        System.out.println("EXECUTE PARRY ATTACK WARNING ");
        System.out.println("[DEFEND] {"+ hero.getName() + "} PAIRED attack made by {"+ pairedHero.getName() +"} left with ["+ hero.getHealth() + "] hp");
        AttackReturnData attackData = AttackCalculations.generateAttackValue(hero, false);

        DefendReturnData pairedHeroDefendData = defend(pairedHero, hero, attackData);

        return new CombatReturnData(new CombatReturnData.AttackDefendData(attackData, pairedHeroDefendData), null);
    }

    public static DefendReturnData defend(BaseHero defender, BaseHero attacker, AttackReturnData attackData){
        DefendReturnData.DefendType defendType = getDefendType(defender);

        Optional<CombatReturnData> parriedData = Optional.empty();
       int effectiveDamage = switch (defendType){
            case PAIRED -> {
                 CombatReturnData data = executeParryAttack(defender, attacker);
                 parriedData = Optional.of(data);
                 yield 0;
            }
           case BLOCKED,DODGED -> 0;
            case NULL-> {
                int effDamage = getCalculatedArmorAbsorption(defender, attackData.baseValues().value());
                defender.decreaseHealth(effDamage);
                yield effDamage;

            }
        };

       //TODO: remove those logs later
        if(defendType.equals(DefendReturnData.DefendType.BLOCKED)) {
            System.out.println("[DEFEND] {"+ defender.getName() + "} BLOCKED attack made by {"+ attacker.getName() +"} left with ["+ defender.getHealth() + "] hp");
        }else if(defendType.equals(DefendReturnData.DefendType.DODGED)){
            System.out.println("[DEFEND] {"+ defender.getName() + "} DODGED attack made by {"+ attacker.getName() +"} left with ["+ defender.getHealth() + "] hp");
        }else if(defendType.equals(DefendReturnData.DefendType.NULL)){
            System.out.println("[DEFEND] {"+ defender.getName() + "} RECEIVED |" + effectiveDamage + "| damage from attack made by {"+ attacker.getName() +"} left with [" + defender.getHealth()+ "] hp");
        }

        return new DefendReturnData(defender.getName(), effectiveDamage, defender.getHealth(), defendType, parriedData);
    }


}
