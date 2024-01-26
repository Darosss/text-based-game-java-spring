package com.example.items.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public enum ItemPrefixesEnum {
    ENCHANTED_ESSENCE("Enchanted Essence");


    private final String displayName;
    private final ItemBaseStatisticsMap statistics ;
    private final ItemAdditionalStatisticsMap additionalStatistics;

    //TODO: do not repeat - move this into utils or something
    private Map<String, PrefixSufixItemStatisticsJsonRecord> initializePrefixMap() {
        InputStream inputStream = ItemPrefixesEnum.class.getClassLoader().getResourceAsStream("itemsdata/itemPrefixes.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            Map<String, PrefixSufixItemStatisticsJsonRecord> value = objectMapper.readValue(inputStream, new TypeReference<
                    Map<String, PrefixSufixItemStatisticsJsonRecord>>() {});
            return value;
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    ItemPrefixesEnum(String displayName) {
        this.displayName = displayName;
        PrefixSufixItemStatisticsJsonRecord currentPrefixData = initializePrefixMap().get(displayName);

        this.statistics = currentPrefixData.baseStatistics();
        this.additionalStatistics = currentPrefixData.additionalStatistics();
    }
    public String getDisplayName() {
        return displayName;
    }
    public ItemBaseStatisticsMap getStatistics() {
        return statistics;
    }

    public ItemAdditionalStatisticsMap getAdditionalStatistics() {
        return additionalStatistics;
    }
}
