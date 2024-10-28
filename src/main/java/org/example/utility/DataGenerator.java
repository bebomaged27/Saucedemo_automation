package org.example.utility;

// DataGenerator.java
import java.util.Random;

public class DataGenerator {
    private static final String[] names = {"John", "Jane", "Alice", "Bob"};
    private static final String[] postalCodes = {"12345", "54321", "123-4567", "abcde"};

    public static String getRandomName() {
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }

    public static String getRandomPostalCode() {
        Random random = new Random();
        return postalCodes[random.nextInt(postalCodes.length)];
    }
}
