package com.example.settings;


//TODO: move those into repository Configs/Settings.
// For now it's programmatically
public class Settings {
    public final static int CHALLENGE_WAIT_COOLDOWN_MINUTES = 2;
    public final static int DUNGEON_WAIT_COOLDOWN_MINUTES = 2;

    public final static int MERCHANT_COMMODITY_REFRESH_HOURS = 24;
    public static final int MERCHANT_VALUE_BUY_COST_COMMODITY_MULTIPLIER = 5;


    public enum MercenaryCharactersLimits {
        FIRST(10, 2),
        SECOND(30, 3),
        THIRD(50, 4);

        private final int requiredLevel;
        private final int charactersLimit;

        MercenaryCharactersLimits(int requiredLevel, int charactersLimit){
            this.requiredLevel = requiredLevel;
            this.charactersLimit = charactersLimit;
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }

        public int getCharactersLimit() {
            return charactersLimit;
        }
    }

}
