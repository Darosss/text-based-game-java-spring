package com.example.characters;

import com.example.characters.equipment.CharacterEquipment;
import com.example.errorhandling.GlobalDefaultExceptionHandler;
import com.example.statistics.AdditionalStatisticsNamesEnum;
import com.example.statistics.BaseStatisticsNamesEnum;
import com.example.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
public class MainCharacter extends Character {
    private static final Logger logger = LoggerFactory.getLogger(MainCharacter.class);

    private Long experience = 0L;

    public MainCharacter() {super();};
    public MainCharacter(String name, User user, CharacterEquipment equipment) {
        super(name, user, equipment);
    }

    public MainCharacter(String name, User user, CharacterEquipment equipment, int level,
                         Map<BaseStatisticsNamesEnum, Integer> baseStatistics,
                         Map<AdditionalStatisticsNamesEnum, Integer> additionalStatistics){
        super(name, user, equipment, level, baseStatistics, additionalStatistics);
    }
    public void gainExperience(long experiencePoints) {
        if (experiencePoints > 0) {
            this.experience += experiencePoints;
            this.checkLevelUp();
        }
    }
    public long getExpToLevelUp () {
        return ExperienceUtils.calculateExpToNextLevel(this.level);
    }

    private void checkLevelUp() {
        long expToLevelUp = this.getExpToLevelUp();
        while (this.experience >= expToLevelUp) {
            this.experience -= expToLevelUp;
            this.levelUp();
            expToLevelUp = ExperienceUtils.calculateExpToNextLevel(this.level);
        }
    }
    private void levelUp() {
        level++;
        this.updateHealthBasedOnMaxHealth();
        logger.debug("Level up! New level: {}", this.level);
    }

    public Long getExperience() {
        return experience;
    }
}