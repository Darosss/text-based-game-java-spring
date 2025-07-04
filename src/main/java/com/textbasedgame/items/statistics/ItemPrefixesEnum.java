package com.textbasedgame.items.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textbasedgame.utils.RandomUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public enum ItemPrefixesEnum {
    ENCHANTED_ESSENCE("Enchanted Essence");


    private final String displayName;
    private final Map<String, ItemStatisticsObject> statistics ;
    private final Map<String, ItemStatisticsObject> additionalStatistics;
    private final List<String> names;
    private final Map<String, PrefixSufixItemStatisticsJsonRecord> PREFIX_DATA = loadPrefixMap();

    private Map<String, PrefixSufixItemStatisticsJsonRecord> loadPrefixMap() {
        try (InputStream inputStream = ItemPrefixesEnum.class.getClassLoader().getResourceAsStream("itemsdata/itemPrefixes.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    ItemPrefixesEnum(String displayName) {
        this.displayName = displayName;
        PrefixSufixItemStatisticsJsonRecord currentPrefixData = this.PREFIX_DATA.get(displayName);
        this.statistics = currentPrefixData.baseStatistics();
        this.additionalStatistics = currentPrefixData.additionalStatistics();
        this.names = currentPrefixData.names();
    }
    public String getDisplayName() {
        return displayName;
    }
    public Map<String, ItemStatisticsObject> getStatistics() {
        return statistics;
    }

    public Map<String, ItemStatisticsObject> getAdditionalStatistics() {
        return additionalStatistics;
    }
    public List<String> getNames() {
        return names;
    }
    public String getRandomName() {
        return RandomUtils.getRandomItemFromArray(this.names.toArray()).toString();
    }
}
