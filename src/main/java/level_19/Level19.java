package level_19;

import common.IntComp;
import common.Level;

import java.util.Objects;
import java.util.Queue;

public class Level19 extends Level {
    private final Drone drone;
    private static final int SQUARE_SIZE = 100;
    private static final boolean VERBOSE = false;

    public Level19(String input) {
        String program = readResourcesFirstLine(input);
        drone = new Drone(program);
    }

    int p1() {
        int result = 0;
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 50; x++) {
                boolean t = drone.move(x, y);
                if (VERBOSE) System.out.print(t ? "#" : '.');
                if (t) result++;
            }
            if (VERBOSE) System.out.print(System.lineSeparator());
        }
        return result;
    }

    long p2() {
        // By scanning 5000x5000 area diagonally find the equation which describes each borderline of a beam as a line
        float leftLineParam = 0;
        float rightLineParam = 0;

        for (int x = 0, y = 5000; x >= 0; x++, y--) {
            boolean t = drone.move(x, y);
            if (t) {
                leftLineParam = y / (float) x;
                break;
            }
        }
        for (int x = 5000, y = 0; y >= 0; x--, y++) {
            boolean t = drone.move(x, y);
            if (t) {
                rightLineParam = y / (float) x;
                break;
            }
        }

        if (VERBOSE) System.out.printf("sl1: %s, sl2: %s%n", leftLineParam, rightLineParam);

        int approxX = -1;
        int approxY = -1;
        // Approximate the square fit position
        for (int topRightX = SQUARE_SIZE; approxX == -1; topRightX++) {
            int topRightY = Math.round(rightLineParam * topRightX);
            int bottomLeftX = topRightX - SQUARE_SIZE;
            int bottomLeftY = topRightY + SQUARE_SIZE;
            if (bottomLeftY == Math.round(leftLineParam * bottomLeftX)) {
                approxX = topRightX - SQUARE_SIZE;
                approxY = bottomLeftY - SQUARE_SIZE;
            }
        }

        if (VERBOSE) System.out.printf("apX: %d, apY: %d%n", approxX, approxY);

        // Now find exact coordinates
        int searchRadius = 10;
        for (int y = approxY - searchRadius; y < approxY + searchRadius; y++) {
            for (int x = approxX - searchRadius; x < approxX + searchRadius; x++) {
                boolean t = canFitSquareAt(x, y);
                if (t) {
                    return 10_000 * x + y;
                }
            }
        }
        return -1;
    }

    boolean canFitSquareAt(int lcx, int lcy) {
        for (int y = lcy; y < lcy + SQUARE_SIZE; y++) {
            boolean t = drone.move(lcx, y);
            if (!t) return false;
        }
        for (int x = lcx; x < lcx + SQUARE_SIZE; x++) {
            boolean t = drone.move(x, lcy);
            if (!t) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Level19 l = new Level19("input");
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2());
    }

    static class Drone {
        final IntComp ic;

        Drone(String program) {
            ic = new IntComp(program, 0);
        }

        /**
         * Move drone to the required position, reporting it is being pulled in there
         *
         * @return true if pulled, false if stationary
         */
        boolean move(int x, int y) {
            ic.addToInput(x);
            ic.addToInput(y);
            ic.run();
            Queue<Long> out = ic.getOutput();
            int o = Objects.requireNonNull(out.poll()).intValue();
            ic.reset();
            if (o != 0 && o != 1) {
                throw new RuntimeException("Unexpected response from bot: " + o);
            }
            return o != 0;
        }
    }
}