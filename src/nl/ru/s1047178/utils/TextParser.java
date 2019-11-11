package nl.ru.s1047178.utils;

import java.util.stream.Stream;

public class TextParser {

    /**
     * Helper method to transform an array of strings into an array of integers
     * @param input
     * @return
     */
    public static int[] getParsedInput(String[] input) {
        return Stream.of(input).mapToInt(Integer::parseInt).toArray();
    }

}
