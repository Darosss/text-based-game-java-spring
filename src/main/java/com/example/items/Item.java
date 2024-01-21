package com.example.items;

import com.example.statistics.StatisticsNamesEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Document(collection = "items")
public class Item {
    @Id
    private String id;
    private String name;

    private String description;

    private Integer level;

    private Integer value;

    private Integer upgradePoints;

    private ItemTypeEnum type;

    private ItemRarityEnum rarity;

    private List<ItemStatisticsObject> statistics;


    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Item(String name, String description, Integer level,
                      Integer value, ItemTypeEnum type, ItemRarityEnum rarity,
                      List<ItemStatisticsObject> statistics) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.value = value;
        this.type = type;
        this.rarity = rarity;
        this.statistics = statistics;
        this.upgradePoints = 0;
    }

    public void setUpgradePoints(Integer upgradePoints) {
        this.upgradePoints = upgradePoints;
    }
    public void setRarity(ItemRarityEnum rarity) {
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getUpgradePoints() {
        return upgradePoints;
    }

    public ItemTypeEnum getType() {
        return type;
    }

    public ItemRarityEnum getRarity() {
        return rarity;
    }

    public List<ItemStatisticsObject> getStatistics() {
        return statistics;
    }

//    public List<StatisticsNamesEnum> getItemStatisticsNames() {
//        List<StatisticsNamesEnum> names = new ArrayList<StatisticsNamesEnum>();
//        for (ItemStatisticsObject stat: statistics ){
//            names.add((stat.getName()));
//        }
//        return names;
//    }

    public Map<StatisticsNamesEnum, List<ItemStatisticsObject>> getStatisticsByName() {
        return statistics.stream()
                .collect(Collectors.groupingBy(ItemStatisticsObject::getName));
    }

//    public String getItemStatisticsNames() {
//        return "Item{" +
//                "statistics=" + statistics +
//                '}';
//    }
}
