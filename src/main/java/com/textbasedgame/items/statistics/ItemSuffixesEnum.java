package com.textbasedgame.items.statistics;

import com.textbasedgame.statistics.BaseStatisticObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textbasedgame.utils.RandomUtils;
import io.swagger.v3.core.util.Json;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public enum ItemSuffixesEnum  {
    OF_CRIMSON_ECLIPSE("of Crimson Eclipse");


    private final String displayName;
    private final Map<String, ItemStatisticsObject> statistics ;
    private final Map<String, ItemStatisticsObject> additionalStatistics;
    private final List<String> names;
    private final Map<String, PrefixSufixItemStatisticsJsonRecord> SUFFIX_DATA = loadSuffixMap();


    private Map<String, PrefixSufixItemStatisticsJsonRecord> loadSuffixMap() {
        try (InputStream inputStream = ItemPrefixesEnum.class.getClassLoader().getResourceAsStream("itemsdata/itemSuffixes.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    ItemSuffixesEnum(String displayName) {
        this.displayName = displayName;
        PrefixSufixItemStatisticsJsonRecord currentSuffixData = this.SUFFIX_DATA.get(displayName);

        this.statistics = currentSuffixData.baseStatistics();
        this.additionalStatistics = currentSuffixData.additionalStatistics();
        this.names = currentSuffixData.names();
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
