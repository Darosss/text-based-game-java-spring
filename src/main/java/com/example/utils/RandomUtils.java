package com.example.utils;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();
    private RandomUtils() {
        // Private constructor to prevent instantiation; utility class should not be instantiated
    }

    public static int getRandomValueWithinRange(int minValue, int maxValue) {
        return Math.min(random.nextInt(maxValue - minValue + 1) + minValue, 1000);
    }

    public static float getRandomFloatValueWithinRange(float minValue, float maxValue) {
        return Math.min(random.nextFloat() * (maxValue - minValue) + minValue, 1000.0f);
    }

    public static <T> T getRandomItemFromArray(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

    public static boolean checkPercentageChance(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }

        double randomValue = random.nextDouble() * 100.0;
        return randomValue <= percentage;
    }
}