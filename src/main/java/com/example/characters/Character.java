package com.example.characters;
import com.example.statistics.BaseStatisticObject;
import com.example.statistics.BaseStatisticsNamesEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Document(collection= "characters")
public class Character {
    @Id
    private String id;
    private Integer level = 0;
    private BaseStatisticObject strength;
    private BaseStatisticObject dexterity;
    private BaseStatisticObject intelligence;
    private BaseStatisticObject constitution;
    private BaseStatisticObject charisma;
    private BaseStatisticObject luck;
    private Long experience = 0L;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Character() {
        //TODO: make parameter and depend on class change basics
        this.strength = new BaseStatisticObject(BaseStatisticsNamesEnum.STRENGTH);
        this.dexterity = new BaseStatisticObject(BaseStatisticsNamesEnum.DEXTERITY);
        this.intelligence = new BaseStatisticObject(BaseStatisticsNamesEnum.INTELLIGENCE);
        this.constitution = new BaseStatisticObject(BaseStatisticsNamesEnum.CONSTITUTION);
        this.charisma = new BaseStatisticObject(BaseStatisticsNamesEnum.CHARISMA);
        this.luck = new BaseStatisticObject(BaseStatisticsNamesEnum.LUCK);    }

    public HashMap<BaseStatisticsNamesEnum, BaseStatisticObject> getBaseStatistics() {


        return new HashMap<BaseStatisticsNamesEnum, BaseStatisticObject>(Map.of(
                strength.getName(), strength,
                dexterity.getName(), dexterity,
                intelligence.getName(), intelligence,
                constitution.getName(), constitution,
                charisma.getName(), charisma,
                luck.getName(), luck
                ));
    }

    public Character(Integer level, Long experience, Integer  basicStats) {
        this.level = level;
        this.experience = experience;
        this.strength.setValue(basicStats);
        this.dexterity.setValue(basicStats);
        this.intelligence.setValue(basicStats);
        this.constitution.setValue(basicStats);
        this.charisma.setValue(basicStats);
        this.luck.setValue(basicStats);
    }


    @Override
    public String toString() {
        return "Character{" +
                "id='" + id + '\'' +
                ", level=" + level +
                ", strength=" + strength +
                ", dexterity=" + dexterity +
                ", intelligence=" + intelligence +
                ", constitution=" + constitution +
                ", charisma=" + charisma +
                ", luck=" + luck +
                ", experience=" + experience +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
