package nl.ru.s1047178.utils;

import java.util.stream.Stream;

public class TextParser {

    // Helper method to get the integers from the raw input
    public static int[] getParsedInput(String[] input) {
        return Stream.of(input).mapToInt(Integer::parseInt).toArray();
    }

}
