package com.example.statistics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsUtils {
    private StatisticsUtils(){};

    public static Map<BaseStatisticsNamesEnum, BaseStatisticObject> generateDefaultHeroStatistics(){
        return new HashMap<>(Arrays.stream(BaseStatisticsNamesEnum.values())
                .collect(Collectors.toMap(stat ->
                        stat,
                        BaseStatisticObject::new))
                );
    }
    public static Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> generateDefaultHeroAdditionalStatistics(){
        return new HashMap<>(Arrays.stream(AdditionalStatisticsNamesEnum.values())
                .collect(Collectors.toMap(
                        stat -> stat,
                        stat -> new AdditionalStatisticObject(stat, stat.getInitialValue())
                ))
        );
    }

    public enum StatisticUpdateType {
        VALUE,
        BONUS,
        PERCENTAGE_BONUS
    }
}
