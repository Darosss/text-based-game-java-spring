package com.example.utils;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();
    private RandomUtils() {
        // Private constructor to prevent instantiation; utility class should not be instantiated
    }

    public static int getRandomValueWithinRange(int minValue, int maxValue) {
        return random.nextInt(maxValue - minValue + 1) + minValue;
    }
    public static double getRandomValueWithinRange(double minValue, double maxValue) {
        return random.nextDouble() * (maxValue - minValue) + minValue;
    }
    public static float getRandomValueWithinRange(float minValue, float maxValue) {
        return random.nextFloat() * (maxValue - minValue) + minValue;
    }
    public static <T> T getRandomItemFromArray(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

    public static boolean checkPercentageChance(double percentage) {
        if(percentage < 0) return false;
        else if(percentage > 100) return true;

        double randomValue = random.nextDouble() * 100.0;
        return randomValue <= percentage;
    }
}