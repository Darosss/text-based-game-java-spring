public enum BaseStatisticsNamesEnum {
    STRENGTH, DEXTERITY, INTELLIGENCE, CONSTITUTION, // ...
}

public abstract class BaseStatistic {
    protected BaseStatisticsNamesEnum name;
    protected int basic;
    protected int bonus;
    protected int max;
1
    // Common methods for statistics
}

public class CharacterStatistic extends BaseStatistic {
    // Specific methods for character base statistics
}

public class AdditionalStatistic extends BaseStatistic {
    // Specific methods for additional statistics
}

public class StatisticModifier {
    private CharacterStatistic baseStat;
    private AdditionalStatistic additionalStat;
    private double modifier;

    public StatisticModifier(CharacterStatistic baseStat, AdditionalStatistic additionalStat, double modifier) {
        this.baseStat = baseStat;
        this.additionalStat = additionalStat;
        this.modifier = modifier;
    }

    public void applyModifier() {
        int modifiedValue = (int)(baseStat.getBasic() + baseStat.getBonus()) * modifier;
        additionalStat.setBonus(additionalStat.getBonus() + modifiedValue);
    }

    // Additional methods or logic related to the modifier
}

public class Character {
    private Map<BaseStatisticsNamesEnum, CharacterStatistic> baseStats;
    private Map<BaseStatisticsNamesEnum, AdditionalStatistic> additionalStats;
    private List<StatisticModifier> statModifiers;

    public Character() {
        // Initialize baseStats, additionalStats, and statModifiers
    }

    public void calculateStats() {
        for (StatisticModifier modifier : statModifiers) {
            modifier.applyModifier();
        }
    }

    // Other character methods
}
