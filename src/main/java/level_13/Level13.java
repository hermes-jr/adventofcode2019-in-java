package level_13;

import common.IntComp;
import common.Level;
import common.Point2D;
import common.Point3D;

import java.util.*;

public class Level13 extends Level {
    final IntComp ic;
    final String prog;
    final Map<Point2D, Integer> screen = new HashMap<>();
    Point3D ball = null;
    Point2D pad;
    long score;
    private final boolean VERBOSE = false;

    public Level13(String input) {
        prog = readResourcesFirstLine(input);
        ic = new IntComp(prog, 0);
    }

    public long p1() {
        step();
        return screen.values().stream().filter(z -> z == 2).count();
    }

    public long p2() {
        ic.reset();
        ic.getData().put(0L, 2L); // start free game
        IntComp.ReturnReason rr;

        do {
            long joy = 0;
            if (ball.getY() != pad.getY() - 1 || ball.getX() != pad.getX()) {
                joy = Integer.compare(ball.getX() + ball.getZ(), pad.getX());
            }

            ic.addToInput(joy);
            rr = step();
            if (VERBOSE) renderScreen(); // next frame

        } while (IntComp.ReturnReason.NO_INPUT.equals(rr));
        return score;
    }

    IntComp.ReturnReason step() {
        IntComp.ReturnReason returnReason = ic.run();
        Queue<Long> out = ic.getOutput();
        for (; !out.isEmpty(); ) {
            int x = Objects.requireNonNull(out.poll()).intValue();
            int y = Objects.requireNonNull(out.poll()).intValue();
            int type = Objects.requireNonNull(out.poll()).intValue();
            Point2D cp = new Point2D(x, y);
            switch (type) {
                case 0:
                case 1:
                case 2:
                    screen.put(cp, type);
                    break;
                case 3:
                    if (pad == null) {
                        pad = cp;
                    }
                    pad.setX(x);
                    screen.put(cp, type);
                    break;
                case 4:
                    if (ball == null) {
                        ball = new Point3D(x, y, 1);
                    } else {
                        ball.setZ(x - ball.getX());
                        ball.setX(x);
                        ball.setY(y);
                    }
                    screen.put(cp, type);
                    break;
                default:
                    if (x == -1 && y == 0) {
                        score = type;
                    } else {
                        throw new RuntimeException("Unknown pixel type: " + type);
                    }
            }
        }
        return returnReason;
    }

    private void renderScreen() {
        StringBuilder result = new StringBuilder(System.lineSeparator());
        int xMax = screen.keySet().stream().max(Comparator.comparingInt(Point2D::getX)).orElse(Point2D.ZERO).getX();
        int yMax = screen.keySet().stream().max(Comparator.comparingInt(Point2D::getY)).orElse(Point2D.ZERO).getY();
        for (int i = 0; i <= yMax; i++) {
            for (int j = 0; j <= xMax; j++) {
                int pixelType = screen.getOrDefault(new Point2D(j, i), 0);
                char pt;
                switch (pixelType) {
                    case 1:
                        pt = '#';
                        break;
                    case 2:
                        pt = 'o';
                        break;
                    case 3:
                        pt = '=';
                        break;
                    case 4:
                        pt = '*';
                        break;
                    case 0:
                    default:
                        pt = ' ';
                }
                result.append(pt);
            }
            result.append(System.lineSeparator());
        }
        result.append(System.lineSeparator())
                .append("Score: ").append(score);
        if (VERBOSE) {
            result.append(System.lineSeparator()).append("Pad: ").append(pad);
            result.append(" Ball: ").append(ball);
        }
        result.append(System.lineSeparator());
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        Level13 l = new Level13("input");
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2());
    }

}