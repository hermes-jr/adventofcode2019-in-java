package level_10;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Level10 {
    final static boolean VERBOSE = false;
    List<Point> asteroids;
    int MAX_X;
    int MAX_Y;

    public Level10(String filename) {
        List<String> s = readResources(filename);
        MAX_X = s.get(0).length();
        MAX_Y = s.size();
        asteroids = parseData(s);
    }

    List<Point> parseData(List<String> input) {
        List<Point> result = new ArrayList<>();

        int y = 0;
        for (String line : input) {
            int x = 0;
            for (Character curChar : line.toCharArray()) {
                if (curChar == '#') {
                    result.add(new Point(x, y));
                }
                x++;
            }
            y++;
        }

        return result;
    }

    int[] p1() {
        Map<Point, Integer> data = new HashMap<>();
        for (Point a : asteroids) {
            data.put(a, getSinglePointVisibleCount(a));
        }
        Map.Entry<Point, Integer> result = data.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow(RuntimeException::new);
        return new int[]{result.getKey().getX(), result.getKey().getY(), result.getValue()};
    }

    public List<String> readResources(String filename) {
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_" + getClass().getSimpleName().substring(5) + "/" + filename))))) {
            List<String> result = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }

    public static void main(String[] args) {
        Level10 l = new Level10("input");
        System.out.println("Part1: " + Arrays.toString(l.p1()));
    }

    public int getSinglePointVisibleCount(Point asteroid) {
        if (VERBOSE) System.out.println("Checking visibility for " + asteroid);
        Set<Point> checked = new HashSet<>();
        int canBeSeen = 0;
        for (Point other : asteroids) {
            if (other.equals(asteroid) || checked.contains(other)) {
                continue;
            }
            if (VERBOSE) System.out.println("Testing " + other + " and all on the same line of sight");
            checked.add(other);
            List<Point> sameLine = new ArrayList<>();
            sameLine.add(asteroid);
            sameLine.add(other);
            int a = asteroid.getY() - other.getY();
            int b = other.getX() - asteroid.getX();
            int c = asteroid.getX() * other.getY() - other.getX() * asteroid.getY();
            if (VERBOSE) System.out.printf("Visibility line: %sx + %sy + %s = 0%n", a, b, c);
            for (Point yetAnother : asteroids) {
                if (yetAnother.equals(asteroid) || yetAnother.equals(other) || checked.contains(yetAnother)) {
                    continue;
                }
                if (isOnSameLine(a, b, c, yetAnother)) {
                    sameLine.add(yetAnother);
                    checked.add(yetAnother);
                }
            }
            if (sameLine.size() > 2) {
                // find only those which are not obstructed
                sameLine.sort(Point.POINT_COMPARATOR);
                if (VERBOSE) System.out.println("Multiple on same line: " + sameLine);
                int selfIdx = sameLine.indexOf(asteroid);
                if (selfIdx > 0) {
                    canBeSeen++;
                }
                if (selfIdx + 1 != sameLine.size()) {
                    canBeSeen++;
                }
                // everything else is obstructed
            } else {
                canBeSeen++;
            }
        }
        if (VERBOSE) System.out.println("Checked: " + checked);
        return canBeSeen;
    }

    // (y1 - y2) x + (x2 - x1) y + (x1y2 - x2y1) = 0
    boolean isOnSameLine(int a, int b, int c, Point p) {
        return a * p.getX() + b * p.getY() + c == 0;
    }

    static class Point implements Comparable<Point> {
        ImmutablePair<Integer, Integer> p;

        final static Comparator<Point> POINT_COMPARATOR = Comparator.comparingInt(Point::getX).thenComparingInt(Point::getY);

        public Point(int x, int y) {
            //noinspection SuspiciousNameCombination
            p = ImmutablePair.of(x, y);
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
            Level10.Point point = (Level10.Point) o;
            return Objects.equals(p, point.p);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p);
        }

        @Override
        public String toString() {
            return "[" + getX() + ":" + getY() + "]";
        }

        @Override
        public int compareTo(Point o) {
            return POINT_COMPARATOR.compare(this, o);
        }
    }
}
