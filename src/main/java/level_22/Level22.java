package level_22;

import common.Level;

import java.util.List;

public class Level22 extends Level {
    int[] deck;
    List<String> commands;

    public Level22(int deckSize, String filename) {
        commands = readResources(filename);
        deck = new int[deckSize];
        for (int i = 0; i < deckSize; i++) {
            deck[i] = i;
        }
    }

    int p1() {
        for (String s : commands) {
            if ("deal into new stack".equals(s)) {
                stack(deck);
            } else if (s.startsWith("cut")) {
                int offset = Integer.parseInt(s.substring(4));
                cut(deck, offset);
            } else if (s.startsWith("deal with increment")) {
                int offset = Integer.parseInt(s.substring(20));
                increment(deck, offset);
            }
        }
        return deck[2019]; // < 9039
    }

    static void increment(int[] deck, int offset) {
        for (int i = 0; i < deck.length; i++) {
            deck[i] = (deck[i] * offset) % deck.length;
        }
    }

    static void cut(int[] deck, int offset) {
        for (int i = 0; i < deck.length; i++) {
            deck[i] = (deck[i] + deck.length - offset) % deck.length;
        }
    }

    static void stack(int[] deck) {
        for (int i = 0; i < deck.length; i++) {
            deck[i] = deck.length - 1 - deck[i];
        }
    }

    public static void main(String[] args) {
        Level22 l = new Level22(10007, "input");
        System.out.println("Part1: " + l.p1());
    }
}
