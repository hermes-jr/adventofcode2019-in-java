package level_17;

import common.IntComp;
import common.Level;

import java.util.Queue;

public class Level17 extends Level {
    IntComp ic;
    String prog;
    boolean[][] map;
    private static final boolean VERBOSE = false;

    public Level17(String input) {
        prog = readResourcesFirstLine(input);
        ic = new IntComp(prog, 0);
    }

    public long p1() {
        ic.run();
        String botOut = readFromBot();
        map = parseData(botOut.substring(0, botOut.length() - 1));
        return findIntersectionsIn(map);
    }

    long findIntersectionsIn(boolean[][] map) {
        int intersections = 0;
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                if (
                        map[i][j] // t
                                && map[i + 1][j] // m
                                && map[i + 2][j] // b
                                && map[i + 1][j - 1]  // l
                                && map[i + 1][j + 1]  // r
                ) {
                    // Cross pattern found, intersection in next line below current position
                    if (VERBOSE) System.out.println("Intersection found at: " + i + ":" + (j - 1));
                    intersections += i * (j - 1);
                }
            }
        }
        return intersections;
    }

    public long p2() {
        long cleaned = 0L;
        ic.reset();
        /*ic.getData().put(0L, 2L);

        feedToBot("A,B,C"); // main
        printBotOutput();
        feedToBot("L,10,R,8,R,6,R,10,L"); // a
        feedToBot("12");  // b
        feedToBot("R");  // c 9x6 square
        feedToBot("y"); // live feed
        cleaned += ic.getOutput().poll();
        printBotOutput();
        */
        System.out.println("cleaned: " + cleaned);
        return -1;
    }

    private void printBotOutput() {
        System.out.println(readFromBot());
    }

    private String readFromBot() {
        Queue<Long> o = ic.getOutput();
        StringBuilder botOutput = new StringBuilder();
        while (!o.isEmpty()) {
            botOutput.append((char) o.poll().intValue());
        }
        return botOutput.toString();
    }

    private void feedToBot(String cmd) {
        for (char z : cmd.toCharArray()) {
            ic.addToInput(z);
        }
        ic.addToInput(10); // Newline in the end
        ic.run();
    }

    public static void main(String[] args) {
        Level17 l = new Level17("input");
        System.out.println("Part1: " + l.p1());
        System.out.println("Part1: " + l.p2());
    }

    public boolean[][] parseData(String in) {
        int w = 0;
        int h = 0;
        char[] asChars = in.toCharArray();

        for (int i = 0; i < asChars.length; i++) {
            if (asChars[i] == '\n') {
                if (w == 0) {
                    w = i;
                }
                h++;
            }
        }
        boolean[][] result = new boolean[h + 2][w + 2];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // All previous newlines + all cells in previous lines + current x
                int aci = i + j + i * w;
                if (aci >= asChars.length) {
                    System.out.printf("w:h %d:%d x:y %d:%d idx %d%n", w, h, j, i, aci);
                }
                if (asChars[aci] != '.') {
                    result[i + 1][j + 1] = true;
                }
            }
        }

        return result;
    }
}