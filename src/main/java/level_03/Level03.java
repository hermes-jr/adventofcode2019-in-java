package level_03;

import common.Level;
import common.Point2D;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class Level03 extends Level {
    public static void main(String[] args) {
        Level03 l = new Level03();
        ImmutablePair<ArrayList<Point2D>, ArrayList<Point2D>> wires = l.processResource("input");
        Set<Point2D> intersections = l.findIntersections(wires);
        int result1 = l.getDistanceToClosestIntersection(intersections);
        System.out.println("Part1: " + result1);
        int result2 = l.getShortestWireLengthToIntersection(wires, intersections);
        System.out.println("Part2: " + result2);
    }

    public ImmutablePair<ArrayList<Point2D>, ArrayList<Point2D>> processResource(String fname) {
        List<String> in = readResources(fname);
        return ImmutablePair.of(parseLine(in.get(0)), parseLine(in.get(1)));
    }

    Set<Point2D> findIntersections(ImmutablePair<ArrayList<Point2D>, ArrayList<Point2D>> wires) {
        Set<Point2D> result = new HashSet<>(wires.getLeft());
        Set<Point2D> second = new HashSet<>(wires.getRight());
        result.retainAll(second);
        result.remove(Point2D.ZERO);
        return result;
    }

    int getShortestWireLengthToIntersection(ImmutablePair<ArrayList<Point2D>, ArrayList<Point2D>> wires, Set<Point2D> intersections) {
        Map<Point2D, Integer> sums = new HashMap<>();
        ArrayList<Point2D> wire1 = wires.getLeft();
        ArrayList<Point2D> wire2 = wires.getRight();

        // trim wire

        int steps = 0;
        for (Point2D z : wire1) {
            if (intersections.contains(z)) {
                sums.put(z, sums.getOrDefault(z, 0) + steps);
            }
            steps++;
        }
        steps = 0;
        for (Point2D z : wire2) {
            if (intersections.contains(z)) {
                sums.put(z, sums.getOrDefault(z, 0) + steps);
            }
            steps++;
        }
        return sums.values().stream().min(Integer::compare).orElse(-1);
    }

    int getDistanceToClosestIntersection(Set<Point2D> intersections) {
        int d = Integer.MAX_VALUE;
        for (Point2D ip : intersections) {
            if (ip.equals(Point2D.ZERO)) {
                continue;
            }
            int nd = Math.abs(ip.getX()) + Math.abs(ip.getY());
            if (nd < d) {
                d = nd;
            }
        }
        return d;
    }

    ArrayList<Point2D> parseLine(String readLine) {
        ArrayList<Point2D> result = new ArrayList<>();
        result.add(Point2D.ZERO);
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
                result.add(new Point2D(x, y));
            }
        }
        return result;
    }

}
