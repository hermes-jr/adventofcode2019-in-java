package level_17;

import common.IntComp;
import common.Level;
import common.Point2D;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Level17 extends Level {
    IntComp ic;
    String prog;
    boolean[][] map;
    Point2D startingPoint;
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

    String tracePath(boolean[][] map) {
        StringBuilder result = new StringBuilder();
        List<Point2D> directions = new LinkedList<>();
        directions.add(new Point2D(-1, 0)); // l
        directions.add(new Point2D(0, -1)); // u
        directions.add(new Point2D(1, 0)); // r
        directions.add(new Point2D(0, 1)); // d

        int x = startingPoint.getX();
        int y = startingPoint.getY();

        while (true) {
            if (map[y + directions.get(0).getY()][x + directions.get(0).getX()]) {
                result.append("L,");
                Collections.rotate(directions, 1);
            } else if (map[y + directions.get(2).getY()][x + directions.get(2).getX()]) {
                result.append("R,");
                Collections.rotate(directions, -1);
            } else {
                break;
            }
            int steps = 0;
            while (map[y + directions.get(1).getY()][x + directions.get(1).getX()]) {
                steps++;
                y += directions.get(1).getY();
                x += directions.get(1).getX();
            }
            result.append(steps).append(',');
        }

        return result.toString();
    }

    public long p2() {
        String wholePath = tracePath(map);

        if (VERBOSE) System.out.println(wholePath);

        ic.reset();
        ic.getData().put(0L, 2L);

        // Shame on me, couldn't automate string compression :-(
        feedToBot("A,B,A,B,C,C,B,A,C,A"); // main
        feedToBot("L,10,R,8,R,6,R,10"); // a
        feedToBot("L,12,R,8,L,12");  // b
        feedToBot("L,10,R,8,R,8");  // c
        feedToBot(VERBOSE ? "y" : "n"); // live feed
        long cleaned = ((LinkedList<Long>) ic.getOutput()).getLast();
        if (VERBOSE) printBotOutput();
        return cleaned;
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
        System.out.println("Part2: " + l.p2());
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
                switch (asChars[aci]) {
                    case '.':
                        continue;
                    case '^':
                        startingPoint = new Point2D(j + 1, i + 1);
                }
                result[i + 1][j + 1] = true;
            }
        }

        return result;
    }
}