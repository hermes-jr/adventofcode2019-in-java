package level_19;

import common.IntComp;
import common.Level;

import java.util.Objects;
import java.util.Queue;

public class Level19 extends Level {
    private Drone drone;
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
        // Find the approximate
        for (int topRightX = 100; approxX == -1; topRightX++) {
            int topRightY = Math.round(rightLineParam * topRightX);
            int bottomLeftX = topRightX - 100;
            int bottomLeftY = topRightY + 100;
            if (bottomLeftY == Math.round(leftLineParam * bottomLeftX)) {
                approxX = topRightX - 100;
                approxY = bottomLeftY - 100;
            }
        }

        if (VERBOSE) System.out.printf("apX: %d, apY: %d%n", approxX, approxY);

        // Now find exact coordinates
        for (int y = approxY - 10; y < approxY + 10; y++) {
            for (int x = approxX - 10; x < approxX + 10; x++) {
                boolean t = canFitSquareAt(x, y);
                if (t) {
                    return 10_000 * x + y;
                }
            }
        }
        return -1;
    }

    boolean canFitSquareAt(int lcx, int lcy) {
        for (int y = lcy; y < lcy + 100; y++) {
            boolean t = drone.move(lcx, y);
            drone.ic.reset();
            if (!t) return false;
        }
        for (int x = lcx; x < lcx + 100; x++) {
            boolean t = drone.move(x, lcy);
            drone.ic.reset();
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
        IntComp ic;

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