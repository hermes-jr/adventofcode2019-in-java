package level_17;

import common.IntComp;
import common.Level;

import java.util.Queue;

public class Level17 extends Level {
    IntComp ic;
    String prog;
    private static final boolean VERBOSE = false;

    public Level17(String input) {
        prog = readResourcesFirstLine(input);
        ic = new IntComp(prog, 0);
    }

    public long p1() {
        ic.run();
        String botOut = readFromBot();
        return findIntersectionsIn(botOut);
    }

    long findIntersectionsIn(String map) {
        int strLen = map.indexOf("\n");
        char[] chars = map.toCharArray();
        int intersections = 0;
        int lineNum = 0;
        for (int i = 1; i < chars.length - (2 * strLen); i++) {
            if (chars[i] == '\n') {
                lineNum++;
                continue;
            }
            if (chars[i] == '#'
                    && chars[i] == '#' // t
                    && chars[i + 1 + strLen] == '#' // m
                    && chars[i + 2 + 2 * strLen] == '#' // b
                    && chars[i + 1 + strLen - 1] == '#' // l
                    && chars[i + 1 + strLen + 1] == '#' // r
            ) {
                // Cross pattern found, intersection in next line below current position
                int x = (i - lineNum) % strLen;
                int y = lineNum + 1;
                if (VERBOSE) System.out.println("Intersection found at: " + x + ":" + y);
                intersections += x * y;
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

}