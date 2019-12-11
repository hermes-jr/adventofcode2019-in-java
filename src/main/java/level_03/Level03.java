package level_03;

import common.Level;
import common.Point;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class Level03 extends Level {
    public static void main(String[] args) {
        Level03 l = new Level03();
        ImmutablePair<ArrayList<Point>, ArrayList<Point>> wires = l.processResource("input");
        Set<Point> intersections = l.findIntersections(wires);
        int result1 = l.getDistanceToClosestIntersection(intersections);
        System.out.println("Part1: " + result1);
        int result2 = l.getShortestWireLengthToIntersection(wires, intersections);
        System.out.println("Part2: " + result2);
    }

    public ImmutablePair<ArrayList<Point>, ArrayList<Point>> processResource(String fname) {
        List<String> in = readResources(Level03.class, fname);
        return ImmutablePair.of(parseLine(in.get(0)), parseLine(in.get(1)));
    }

    Set<Point> findIntersections(ImmutablePair<ArrayList<Point>, ArrayList<Point>> wires) {
        Set<Point> result = new HashSet<>(wires.getLeft());
        Set<Point> second = new HashSet<>(wires.getRight());
        result.retainAll(second);
        result.remove(Point.ZERO);
        return result;
    }

    int getShortestWireLengthToIntersection(ImmutablePair<ArrayList<Point>, ArrayList<Point>> wires, Set<Point> intersections) {
        Map<Point, Integer> sums = new HashMap<>();
        ArrayList<Point> wire1 = wires.getLeft();
        ArrayList<Point> wire2 = wires.getRight();

        // trim wire

        int steps = 0;
        for (Point z : wire1) {
            if (intersections.contains(z)) {
                sums.put(z, sums.getOrDefault(z, 0) + steps);
            }
            steps++;
        }
        steps = 0;
        for (Point z : wire2) {
            if (intersections.contains(z)) {
                sums.put(z, sums.getOrDefault(z, 0) + steps);
            }
            steps++;
        }
        return sums.values().stream().min(Integer::compare).orElse(-1);
    }

    int getDistanceToClosestIntersection(Set<Point> intersections) {
        int d = Integer.MAX_VALUE;
        for (Point ip : intersections) {
            if (ip.equals(Point.ZERO)) {
                continue;
            }
            int nd = Math.abs(ip.getX()) + Math.abs(ip.getY());
            if (nd < d) {
                d = nd;
            }
        }
        return d;
    }

    ArrayList<Point> parseLine(String readLine) {
        ArrayList<Point> result = new ArrayList<>();
        result.add(Point.ZERO);
        int x = 0;
        int y = 0;
        for (String cmd : readLine.split(",")) {
            char direction = cmd.charAt(0);
            int distance = Integer.parseInt(cmd.substring(1));
            int dx = 0, dy = 0;
            switch (direction) {
                case 'L':
                    dx = -1;
                    break;
                case 'R':
                    dx = 1;
                    break;
                case 'U':
                    dy = 1;
                    break;
                case 'D':
                    dy = -1;
                    break;
            }
            for (int i = 0; i < distance; i++) {
                x += dx;
                y += dy;
                result.add(new Point(x, y));
            }
        }
        return result;
    }

}
