package level_11;

import common.IntComp;
import common.Level;
import common.Point2D;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

public class Level11 extends Level {
    IntComp ic;
    final String prog;
    final Map<Point2D, Long> plate = new HashMap<>();
    static final List<ImmutablePair<Integer, Integer>> directions = new LinkedList<>();

    private static void initDirections() {
        directions.clear();
        directions.add(ImmutablePair.of(0, -1));
        directions.add(ImmutablePair.of(1, 0));
        directions.add(ImmutablePair.of(0, 1));
        directions.add(ImmutablePair.of(-1, 0));
    }

    public Level11(String input) {
        initDirections();
        prog = readResourcesFirstLine(input);
    }

    public int p1() {
        ic = new IntComp(prog, 0);
        paint(0L);
        return plate.size();
    }

    public String p2() {
        plate.clear();
        initDirections();
        ic = new IntComp(prog, 1);
        paint(1L);
        return plateToString();
    }

    private void paint(long initialPixelState) {
        plate.put(Point2D.ZERO, initialPixelState);
        IntComp.ReturnReason lastReason = null;
        Point2D currentCoordinate = Point2D.ZERO;
        while (lastReason != IntComp.ReturnReason.HALTED) {
            ic.addToInput(plate.getOrDefault(currentCoordinate, 0L));
            lastReason = ic.run();
            Long color = ic.getOutput().poll();
            Long direction = ic.getOutput().poll();
            plate.put(currentCoordinate, color);
            // move
            Collections.rotate(directions, Long.valueOf(1L).equals(direction) ? 1 : -1);
            int newX = currentCoordinate.getX() + directions.get(0).getLeft();
            int newY = currentCoordinate.getY() + directions.get(0).getRight();
            currentCoordinate = new Point2D(newX, newY);
        }
    }

    private String plateToString() {
        StringBuilder result = new StringBuilder(System.lineSeparator());
        IntSummaryStatistics xStats = plate.keySet().stream().collect(Collectors.summarizingInt(Point2D::getX));
        IntSummaryStatistics yStats = plate.keySet().stream().collect(Collectors.summarizingInt(Point2D::getY));
        for (int i = yStats.getMin(); i <= yStats.getMax(); i++) {
            for (int j = xStats.getMax(); j >= xStats.getMin(); j--) {
                long pixelColor = plate.getOrDefault(new Point2D(j, i), 0L);
                result.append(pixelColor == 0L ? "  " : " #");
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Level11 l = new Level11("input");
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2());
    }

}