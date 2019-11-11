package nl.ru.s1047178;

import nl.ru.s1047178.utils.TextParser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String[] rawInput = input.nextLine().split(" ");
        int[] parsedInput = TextParser.getParsedInput(rawInput);
        int n = parsedInput[0];
        int m = parsedInput[1];
        int k = parsedInput[2];

        GarbageCollection gc = new GarbageCollection(n, m, k);
        String possible = gc.isPossible() ? "possible" : "impossible";
        System.out.println(possible);
    }

}
