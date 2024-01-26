package com.example.utils;

import java.util.Random;

public class RandomUtils {

    private RandomUtils() {
        // Private constructor to prevent instantiation; utility class should not be instantiated
    }

    public static int getRandomValueWithinRange(int minValue, int maxValue) {
        Random random = new Random();
        return Math.min(random.nextInt(maxValue - minValue + 1) + minValue, 1000);
    }

    public static float getRandomFloatValueWithinRange(float minValue, float maxValue) {
        Random random = new Random();
        return Math.min(random.nextFloat() * (maxValue - minValue) + minValue, 1000.0f);
    }

    public static <T> T getRandomItemFromArray(T[] array) {
        Random random = new Random();
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }
}