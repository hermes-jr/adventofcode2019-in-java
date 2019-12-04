package level_03;

import level_01.Level01;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Level03 {
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
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Level01.class.getClassLoader().getResourceAsStream("level_03/" + fname))))) {
            return new ImmutablePair<>(parseLine(br.readLine()), parseLine(br.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unparseable");
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

    static class Point {
        ImmutablePair<Integer, Integer> p;
        final static Point ZERO = new Point(0, 0);

        public Point(int x, int y) {
            //noinspection SuspiciousNameCombination
            p = new ImmutablePair<>(x, y);
        }

        public int getX() {
            return p.getLeft();
        }

        public int getY() {
            return p.getRight();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Objects.equals(p, point.p);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p);
        }
    }

}
