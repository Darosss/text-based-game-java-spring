package com.example.characters;
import com.example.characters.equipment.CharacterEquipment;
import com.example.statistics.AdditionalStatisticObject;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticObject;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.users.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Document(collection= "characters")
public class Character {
    @Id
    private String id;
    private Integer level = 0;
    private Map<BaseStatisticsNamesEnum, BaseStatisticObject> statistics;
    private Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> additionalStatistics;

    @DBRef
    private User user;
    @DBRef
    private CharacterEquipment equipment;
    private Long experience = 0L;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Character() {}

    public Character(User user, CharacterEquipment equipment) {
        //TODO: make parameter and depend on class change basics
        this.statistics = new HashMap<>(Map.of(
                BaseStatisticsNamesEnum.STRENGTH, new BaseStatisticObject(BaseStatisticsNamesEnum.STRENGTH),
                BaseStatisticsNamesEnum.DEXTERITY, new BaseStatisticObject(BaseStatisticsNamesEnum.DEXTERITY),
                BaseStatisticsNamesEnum.INTELLIGENCE, new BaseStatisticObject(BaseStatisticsNamesEnum.INTELLIGENCE),
                BaseStatisticsNamesEnum.CONSTITUTION, new BaseStatisticObject(BaseStatisticsNamesEnum.CONSTITUTION),
                BaseStatisticsNamesEnum.CHARISMA, new BaseStatisticObject(BaseStatisticsNamesEnum.CHARISMA),
                BaseStatisticsNamesEnum.LUCK, new BaseStatisticObject(BaseStatisticsNamesEnum.LUCK)
        ));
        this.additionalStatistics = new HashMap<>(Map.of(
                AdditionalStatisticsNamesEnum.ARMOR, new AdditionalStatisticObject(AdditionalStatisticsNamesEnum.ARMOR),
                AdditionalStatisticsNamesEnum.MAX_DAMAGE, new AdditionalStatisticObject(AdditionalStatisticsNamesEnum.MAX_DAMAGE),
                AdditionalStatisticsNamesEnum.MIN_DAMAGE, new AdditionalStatisticObject(AdditionalStatisticsNamesEnum.MIN_DAMAGE)
        ));
        //For set equipment here and character in equipment
        this.equipment = equipment;
        this.user = user;

    }

    public Character(User user, CharacterEquipment equipment, int level, long experience, int  basicStats, int armor, int minDmg, int maxDmg) {
        this.level = level;
        //TODO: tbh its for debug, ignore
        this.experience = experience;
        this.statistics = new HashMap<BaseStatisticsNamesEnum, BaseStatisticObject>(Map.of(
                BaseStatisticsNamesEnum.STRENGTH, new BaseStatisticObject(BaseStatisticsNamesEnum.STRENGTH, basicStats),
                BaseStatisticsNamesEnum.DEXTERITY, new BaseStatisticObject(BaseStatisticsNamesEnum.DEXTERITY, basicStats),
                BaseStatisticsNamesEnum.INTELLIGENCE, new BaseStatisticObject(BaseStatisticsNamesEnum.INTELLIGENCE, basicStats),
                BaseStatisticsNamesEnum.CONSTITUTION, new BaseStatisticObject(BaseStatisticsNamesEnum.CONSTITUTION, basicStats),
                BaseStatisticsNamesEnum.CHARISMA, new BaseStatisticObject(BaseStatisticsNamesEnum.CHARISMA, basicStats),
                BaseStatisticsNamesEnum.LUCK, new BaseStatisticObject(BaseStatisticsNamesEnum.LUCK, basicStats)
        ));
        this.additionalStatistics = new HashMap<AdditionalStatisticsNamesEnum, AdditionalStatisticObject>(Map.of(
                AdditionalStatisticsNamesEnum.ARMOR, new AdditionalStatisticObject(AdditionalStatisticsNamesEnum.ARMOR, armor),
                AdditionalStatisticsNamesEnum.MAX_DAMAGE, new AdditionalStatisticObject(AdditionalStatisticsNamesEnum.MAX_DAMAGE, minDmg),
                AdditionalStatisticsNamesEnum.MIN_DAMAGE, new AdditionalStatisticObject(AdditionalStatisticsNamesEnum.MIN_DAMAGE, maxDmg)
        ));
        //For set equipment here and character in equipment
        this.equipment = equipment;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Character{" +
                "id='" + id + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public String getId() {
        return id;
    }

    public Integer getLevel() {
        return level;
    }

    public Map<BaseStatisticsNamesEnum, BaseStatisticObject> getStatistics() {
        return statistics;
    }

    public Map<AdditionalStatisticsNamesEnum, AdditionalStatisticObject> getAdditionalStatistics() {
        return additionalStatistics;
    }

    public Long getExperience() {
        return experience;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CharacterEquipment getEquipment() {
        return equipment;
    }


}
