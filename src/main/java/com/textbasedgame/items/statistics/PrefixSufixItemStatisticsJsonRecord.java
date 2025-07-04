package com.textbasedgame.items.statistics;

import java.util.List;
import java.util.Map;

public record PrefixSufixItemStatisticsJsonRecord(Map<String, ItemStatisticsObject> baseStatistics, Map<String, ItemStatisticsObject> additionalStatistics, List<String> names) {}
