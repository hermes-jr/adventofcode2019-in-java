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
                drone.ic.reset();
            }
            if (VERBOSE) System.out.print(System.lineSeparator());
        }
        return result;
    }

    int p2() {
        for (int y = 500; y < 10000; y++) {
            for (int x = 100; x < 8000; x++) {
                boolean t = canFitSquareAt(x, y);
                if (t) {
                    return 10000 * x + y;
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
            if (o != 0 && o != 1) {
                throw new RuntimeException("Unexpected response from bot: " + o);
            }
            return o != 0;
        }
    }
}