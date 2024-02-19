package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.items.ItemMercenary;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.statistics.StatisticsUtils;
import com.example.users.User;
import dev.morphia.annotations.Reference;

import javax.annotation.Nullable;

public class MercenaryCharacter extends Character{
    @Reference(idOnly = true, lazy = true)
    private ItemMercenary mercenary;
    public MercenaryCharacter() {
    }

    public MercenaryCharacter(String name, User user, CharacterEquipment equipment) {
        super(name, user, equipment);
    }

    public void setMercenary(@Nullable ItemMercenary mercenaryValue) {
        if(mercenaryValue == null) {
            this.level = 1;
            this.setStatisticsOnUnEquipMercenary();
        }else {
            this.level = mercenaryValue.getLevel();
            this.setStatisticsOnEquipMercenary(mercenaryValue);
        }
        this.mercenary = mercenaryValue;
    }

    public ItemMercenary getMercenary() {
        return mercenary;
    }

    private void setStatisticsOnEquipMercenary(ItemMercenary equippedMercenary) {
        equippedMercenary.getStatistics().getBaseStatistics().forEach((k,v)->{
            this.stats.updateStatistic(BaseStatisticsNamesEnum.valueOf(k), v.getValue(), StatisticsUtils.StatisticUpdateType.VALUE);
        });
        equippedMercenary.getStatistics().getAdditionalStatistics().forEach((k,v)->{
            this.stats.updateAdditionalStatistic(AdditionalStatisticsNamesEnum.valueOf(k), v.getValue(), StatisticsUtils.StatisticUpdateType.VALUE);
        });
    }
    private void setStatisticsOnUnEquipMercenary() {
        this.stats.getAdditionalStatistics().forEach((k,v)->
                this.stats.updateAdditionalStatistic(k, 1, StatisticsUtils.StatisticUpdateType.VALUE));
        this.stats.getStatistics().forEach((k,v)->
                this.stats.updateStatistic(k, 1, StatisticsUtils.StatisticUpdateType.VALUE));
    }
}
