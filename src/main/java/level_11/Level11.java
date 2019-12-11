package level_11;

import common.IntComp;
import common.Level;
import common.Point;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class Level11 extends Level {
    IntComp ic;
    Map<Point, Long> plate = new HashMap<>();
    static List<ImmutablePair<Integer, Integer>> directions = new LinkedList<>();

    static {
        directions.add(ImmutablePair.of(0, -1));
        directions.add(ImmutablePair.of(1, 0));
        directions.add(ImmutablePair.of(0, 1));
        directions.add(ImmutablePair.of(-1, 0));
    }

    public Level11(String input) {
        String prog = readResourcesFirstLine(Level11.class, input);
        ic = new IntComp(prog, 0);
    }

    public int p1() {
        paint();
        return plate.size();
    }

    private void paint() {
        IntComp.ReturnReason lastReason = null;
        Point currentCoordinate = Point.ZERO;
        while (lastReason != IntComp.ReturnReason.HALTED) {
            ic.addToInput(plate.getOrDefault(currentCoordinate, 0L));
            lastReason = ic.run();
            long color = ic.getOutput().poll();
            long direction = ic.getOutput().poll();
            plate.put(currentCoordinate, color);
            // move
            Collections.rotate(directions, direction == 1L ? 1 : -1);
            int newX = currentCoordinate.getX() + directions.get(0).getLeft();
            int newY = currentCoordinate.getY() + directions.get(0).getRight();
            currentCoordinate = new Point(newX, newY);
        }
        System.out.println(plate);
    }

    public static void main(String[] args) {
        Level11 l = new Level11("input");
        System.out.println("Part1: " + l.p1());
    }

}