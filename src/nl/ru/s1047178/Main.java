package nl.ru.s1047178;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String[] rawInput = input.nextLine().split(" ");
        int[] parsedInput = TextParser.getParsedInput(rawInput);
        int n = parsedInput[0];
        int m = parsedInput[1];
        int k = parsedInput[2];
        int streets [][] = new int[n][2];
        for (int i = 0; i < n; i++) {
        streets[i] = TextParser.getParsedInput(input.nextLine().split(" "));
        }

        GarbageCollection gc = new GarbageCollection(n, m, k,streets);
        String possible = gc.isPossible() ? "possible" : "impossible";
        System.out.println(possible);
    }
}
