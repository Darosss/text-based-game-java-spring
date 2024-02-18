package com.example.items.statistics;

import com.example.items.ItemRarityEnum;
import com.example.items.ItemUtils;
import com.example.items.ItemsSubtypes;
import dev.morphia.annotations.ExternalEntity;
import org.springframework.data.util.Pair;
import java.util.*;


//TODO: make this class more readable later xpp

@ExternalEntity(target = ItemStatistics.class)
public class ItemStatistics {
    private final Map<String, ItemStatisticsObject> baseStatistics = new HashMap<>();
    private final Map<String, ItemStatisticsObject> additionalStatistics = new HashMap<>();


    public ItemStatistics(){}
    public ItemStatistics(
            Map<String, ItemStatisticsObject> baseStatistics, Map<String, ItemStatisticsObject> baseAdditionalStatistics,
            ItemPrefixesEnum prefix, ItemSuffixesEnum suffix, int itemLevel, ItemRarityEnum itemRarity, ItemsSubtypes subtype
    ) {
        this.baseStatistics.putAll(baseStatistics);
        this.additionalStatistics.putAll(baseAdditionalStatistics);

        this.baseStatistics.putAll(this.getMergedStatsWithPrefixSuffix(baseStatistics, prefix, suffix));
        this.additionalStatistics.putAll(this.getMergedAdditionalStatsWithPrefixSuffix(baseStatistics, prefix, suffix));

        this.handleBaseStatisticsUpdateBasedOnSubtype(itemLevel, subtype);
        this.handleAdditionalStatisticsUpdateBasedOnSubtype(itemLevel, subtype);

        this.handleStatisticsUpdateBasedOnRarity(itemRarity);
    }

    /**
     * Constructor mostly used for consumables and mercenaries items, without prefix, suffix
     *
     */
    public ItemStatistics(
            Map<String, ItemStatisticsObject> baseStatistics, Map<String, ItemStatisticsObject> baseAdditionalStatistics,
            int itemLevel, ItemRarityEnum itemRarity, ItemsSubtypes subtype
    ){
        this.baseStatistics.putAll(baseStatistics);
        this.additionalStatistics.putAll(baseAdditionalStatistics);
        this.handleAdditionalStatisticsUpdateBasedOnSubtype(itemLevel, subtype);
        this.handleBaseStatisticsUpdateBasedOnSubtype(itemLevel, subtype);
        this.handleStatisticsUpdateBasedOnRarity(itemRarity);

    }


    private Map<String, ItemStatisticsObject> mergeStatsFromPrefixSuffix(ItemPrefixesEnum prefix, ItemSuffixesEnum suffix) {
        return ItemUtils.getMergedItemStatisticsObjectMaps(prefix.getStatistics(), suffix.getStatistics());
    }

    private Map<String, ItemStatisticsObject> mergeAdditionalStatsFromPrefixSuffix(ItemPrefixesEnum prefix, ItemSuffixesEnum suffix) {
       return ItemUtils.getMergedItemStatisticsObjectMaps(prefix.getAdditionalStatistics(), suffix.getAdditionalStatistics());

    }
    private Map<String, ItemStatisticsObject> getMergedStatsWithPrefixSuffix(
            Map<String, ItemStatisticsObject> baseStatistics, ItemPrefixesEnum prefix, ItemSuffixesEnum suffix
    ) {
        Map<String, ItemStatisticsObject> prefixSuffixBaseStats = this.mergeStatsFromPrefixSuffix(prefix, suffix);
        return ItemUtils.getMergedItemStatisticsObjectMaps(baseStatistics, prefixSuffixBaseStats);
    }

    private Map<String, ItemStatisticsObject> getMergedAdditionalStatsWithPrefixSuffix(
            Map<String, ItemStatisticsObject> additionalStatistics,
            ItemPrefixesEnum prefix, ItemSuffixesEnum suffix) {
        Map<String, ItemStatisticsObject> prefixSuffixAdditionalStats = this.mergeAdditionalStatsFromPrefixSuffix(prefix, suffix);
        return ItemUtils.getMergedItemStatisticsObjectMaps(additionalStatistics, prefixSuffixAdditionalStats);

    }

    private List<Pair<Double, Double>> prepareStatsAdjustmentBasedOnItemType(ItemRarityEnum type) {
        List<Pair<Double, Double>> values = new ArrayList<>();
        for(ItemRarityEnum value: ItemRarityEnum.values()){
            values.add(Pair.of(value.getBonusValue(), value.getBonusPercentageValue()));
            if(type.equals(value)) break;
        }

        return values;
    }
    private void handleStatisticsUpdateBasedOnRarity(ItemRarityEnum rarity){
        List<Pair<Double, Double>> adjustments = this.prepareStatsAdjustmentBasedOnItemType(rarity);
        adjustments.forEach((v)-> {
            double valuePercent = v.getFirst() / 100; double valuePercentagePercent = v.getSecond() / 100;

            this.baseStatistics.keySet().forEach((key)->{
                this.baseStatistics.get(key).increaseValueByPercent(valuePercent);
                this.baseStatistics.get(key).increasePercentageValueByPercent(valuePercentagePercent);
            });

            this.additionalStatistics.keySet().forEach((key)->{
                this.additionalStatistics.get(key).increaseValueByPercent(valuePercent);
                this.additionalStatistics.get(key).increasePercentageValueByPercent(valuePercentagePercent);
            });
        });
    }


    private void handleAdditionalStatisticsUpdateBasedOnSubtype(int itemLevel, ItemsSubtypes subtype) {
        subtype.getAdditionalStatisticsPerLevel().forEach((k, v)-> {
            int subtypeStatValue = (int) (v*itemLevel);
            if(this.additionalStatistics.containsKey(k.toString())) {
                this.additionalStatistics.get(k.toString()).increaseValue(subtypeStatValue);
            }else {
                this.additionalStatistics.put(k.toString(), new ItemStatisticsObject(k.toString(), subtypeStatValue, 0));
            }
        });
    }
    private void handleBaseStatisticsUpdateBasedOnSubtype(int itemLevel, ItemsSubtypes subtype) {
        subtype.getBaseStatisticsPerLevel().forEach((k, v)-> {
            int subtypeStatValue = (int) (v*itemLevel);
            if(this.baseStatistics.containsKey(k.toString())) {
                this.baseStatistics.get(k.toString()).increaseValue(subtypeStatValue);
            }else {
                this.baseStatistics.put(k.toString(), new ItemStatisticsObject(k.toString(), subtypeStatValue, 0));
            }
        });
    }


    private void handleStatisticsUpdateBasedOnLevel(int itemLevel) {
        double increaseValueFactor = ItemUtils.getItemLevelFactorValueStatistics(itemLevel) / 100;
        double increasePercentValueFactor = ItemUtils.getItemLeveFactorPercentStatistics(itemLevel) / 100;
        this.baseStatistics.keySet().forEach((key)->{
            ItemStatisticsObject currentStat = this.baseStatistics.get(key);
            currentStat.increaseValue((int)(currentStat.getValue() * increaseValueFactor));
            currentStat.increasePercentageValue((int)(currentStat.getPercentageValue() * increasePercentValueFactor));
        });

        this.additionalStatistics.keySet().forEach((key)->{
            ItemStatisticsObject currentStat = this.additionalStatistics.get(key);
            currentStat.increaseValue((int)(currentStat.getValue() * increaseValueFactor));
            currentStat.increasePercentageValue((int)(currentStat.getPercentageValue() * increasePercentValueFactor));
        });
    }

    public Map<String, ItemStatisticsObject> getBaseStatistics() {
        return baseStatistics;
    }

    public Map<String, ItemStatisticsObject> getAdditionalStatistics() {
        return additionalStatistics;
    }
}
