package level_22;

import common.Level;

import java.math.BigInteger;
import java.util.List;

public class Level22 extends Level {
    int card2019 = 2019;
    final int deckSize;
    final BigInteger cards = BigInteger.valueOf(119315717514047L);
    final BigInteger repeats = BigInteger.valueOf(101741582076661L);
    final List<String> commands;

    public Level22(int deckSize, String filename) {
        commands = readResources(filename);
        this.deckSize = deckSize;
    }

    int p1() {
        for (String s : commands) {
            if ("deal into new stack".equals(s)) {
                card2019 = deckSize - 1 - card2019;
            } else if (s.startsWith("cut")) {
                int offset = Integer.parseInt(s.substring(4));
                card2019 = (card2019 + deckSize - offset) % deckSize;
            } else if (s.startsWith("deal with increment")) {
                int offset = Integer.parseInt(s.substring(20));
                card2019 = (card2019 * offset) % deckSize;
            }
        }
        return card2019;
    }

    // Hell no! I'm stealing this. Not brilliant enough to figure out by myself :-(
    // This is mcpower's algorithm ported from Python
    BigInteger p2() {
        BigInteger increment_mul = BigInteger.ONE;
        BigInteger offset_diff = BigInteger.ZERO;

        for (String s : commands) {
            if ("deal into new stack".equals(s)) {
                increment_mul = increment_mul.multiply(BigInteger.valueOf(-1));
                increment_mul = increment_mul.remainder(cards);
                offset_diff = offset_diff.add(increment_mul);
                offset_diff = offset_diff.remainder(cards);
            } else if (s.startsWith("cut")) {
                long param = Long.parseLong(s.substring(4));
                offset_diff = offset_diff.add(BigInteger.valueOf(param).multiply(increment_mul));
                offset_diff = offset_diff.remainder(cards);
            } else if (s.startsWith("deal with increment")) {
                long param = Long.parseLong(s.substring(20));
                increment_mul = increment_mul.multiply(inv(BigInteger.valueOf(param)));
                increment_mul = increment_mul.remainder(cards);
            }
        }

        BigInteger increment = increment_mul.modPow(repeats, cards);
        BigInteger offset = offset_diff.multiply(BigInteger.ONE.subtract(increment)).multiply(inv(BigInteger.ONE.subtract(increment_mul).remainder(cards)));
        offset = offset.remainder(cards);
        return offset.add(increment.multiply(BigInteger.valueOf(2020L))).remainder(cards);
    }

    private BigInteger inv(BigInteger n) {
        return n.modInverse(cards);
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
        System.out.println("Part2: " + l.p2());
    }
}
