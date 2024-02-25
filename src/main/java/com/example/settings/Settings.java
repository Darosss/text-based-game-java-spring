package com.example.settings;


//TODO: move those into repository Configs/Settings.
// For now it's programmatically
public class Settings {
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
