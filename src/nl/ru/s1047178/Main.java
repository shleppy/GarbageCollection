package nl.ru.s1047178;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String[] rawInput = input.nextLine().split(" ");
        int[] parsedInput = getParsedInput(rawInput);
        int n = parsedInput[0];
        int m = parsedInput[1];
        int k = parsedInput[2];

        GarbageCollection gc = new GarbageCollection(n, m, k);
        String possible = gc.isPossible() ? "possible" : "impossible";
        System.out.println(possible);
    }

    // Helper method to get the integers from the raw input
    private static int[] getParsedInput(String[] input) {
        return Stream.of(input).mapToInt(Integer::parseInt).toArray();
    }

}
