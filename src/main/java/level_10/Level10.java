package level_10;

import common.Level;
import common.Point2D;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Level10 extends Level {
    final static boolean VERBOSE = false;
    List<Point2D> asteroids;
    SortedMap<BigDecimal, List<Point2D>> lines = new TreeMap<>();
    int MAX_X;
    int MAX_Y;

    public Level10(String filename) {
        List<String> s = readResources(filename);
        MAX_X = s.get(0).length();
        MAX_Y = s.size();
        asteroids = parseData(s);
    }

    public static void main(String[] args) {
        Level10 l = new Level10("input");
        int[] r1 = l.p1();
        System.out.println("Part1: " + r1[2]);
        System.out.println("Part2: " + l.p2(new Point2D(r1[0], r1[1])));
    }

    List<Point2D> parseData(List<String> input) {
        List<Point2D> result = new ArrayList<>();

        int y = 0;
        for (String line : input) {
            int x = 0;
            for (Character curChar : line.toCharArray()) {
                if (curChar == '#') {
                    result.add(new Point2D(x, y));
                }
                x++;
            }
            y++;
        }

        return result;
    }

    int[] p1() {
        Map<Point2D, Integer> data = new HashMap<>();
        for (Point2D a : asteroids) {
            data.put(a, getSinglePointVisibleCount(a));
        }
        Map.Entry<Point2D, Integer> result = data.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow(RuntimeException::new);
        return new int[]{result.getKey().getX(), result.getKey().getY(), result.getValue()};
    }

    int p2(Point2D vantagePoint) {
        lines.clear();
        Point2D result = null;
        recalculateAnglesForPoint(vantagePoint);
        if (VERBOSE) System.out.println("Lines for vp: " + vantagePoint + " " + lines);
        int eliminatedAsteroids = 0;
        int totalFoes = Math.min(200, asteroids.size() - 1);
        int revolution = -1;
        outerLoop:
        while (eliminatedAsteroids < totalFoes) {
            for (Map.Entry<BigDecimal, List<Point2D>> entry : lines.entrySet()) {
                int a = entry.getKey().compareTo(BigDecimal.ZERO) == 0 ? 1 : -1;
                Optional<Point2D> point = eliminatePoint(vantagePoint, entry.getValue(), a * revolution);
                if (eliminatedAsteroids >= totalFoes) {
                    break outerLoop;
                }
                if (point.isPresent()) {
                    result = point.get();
                    eliminatedAsteroids++;
                    if (VERBOSE)
                        System.out.printf("%3s Vaporizing %s at rev. %3s%n", eliminatedAsteroids, result, revolution);
                }
            }
            revolution *= -1;
        }
        assert result != null;
        return result.getX() * 100 + result.getY();
    }

    private Optional<Point2D> eliminatePoint(Point2D self, List<Point2D> line, int direction) {
        int selfIdx = line.indexOf(self);
        int targetIdx = selfIdx + direction;
        if (line.size() == 1 || targetIdx < 0 || targetIdx >= line.size()) {
            return Optional.empty();
        } else {
            Optional<Point2D> result = Optional.of(line.get(targetIdx));
            line.remove(targetIdx);
            return result;
        }
    }

    int getSinglePointVisibleCount(Point2D asteroid) {
        return getSinglePointVisibleCount(asteroid, false);
    }

    void recalculateAnglesForPoint(Point2D asteroid) {
        getSinglePointVisibleCount(asteroid, true);
    }

    int getSinglePointVisibleCount(Point2D asteroid, boolean updateLines) {
        if (VERBOSE) System.out.println("Checking visibility for " + asteroid);
        Set<Point2D> checked = new HashSet<>();
        int canBeSeen = 0;
        for (Point2D other : asteroids) {
            if (other.equals(asteroid) || checked.contains(other)) {
                continue;
            }
            if (VERBOSE) System.out.println("Testing " + other + " and all on the same line of sight");
            checked.add(other);
            List<Point2D> sameLine = new ArrayList<>();
            sameLine.add(asteroid);
            sameLine.add(other);
            int x1 = asteroid.getX();
            int x2 = other.getX();
            int y1 = asteroid.getY();
            int y2 = other.getY();

            int a = y1 - y2;
            int b = x2 - x1;
            int c = x1 * y2 - x2 * y1;
            if (VERBOSE) System.out.printf("Line of sight: %sx + %sy + %s = 0%n", a, b, c);
            for (Point2D yetAnother : asteroids) {
                if (yetAnother.equals(asteroid) || yetAnother.equals(other) || checked.contains(yetAnother)) {
                    continue;
                }
                if (isOnSameLine(a, b, c, yetAnother)) {
                    sameLine.add(yetAnother);
                    checked.add(yetAnother);
                }
            }

            sameLine.sort(Point2D.POINT_COMPARATOR);

            if (updateLines) {
                lines.put(BigDecimal.valueOf(getAngleFromPoint(x1, y1, x2, y2)).setScale(4, RoundingMode.HALF_UP), sameLine);
            }

            if (sameLine.size() > 2) {
                // find only those which are not obstructed
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
    boolean isOnSameLine(int a, int b, int c, Point2D p) {
        return a * p.getX() + b * p.getY() + c == 0;
    }

    public double getAngleFromPoint(int x1, int y1, int x2, int y2) {
        if (x2 > x1) {
            return Math.toDegrees(Math.atan2(x2 - x1, y1 - y2));
        } else if (x2 < x1) {
            return Math.toDegrees(2.0 * Math.PI - Math.atan2(x1 - x2, y1 - y2));
        }
        return 0.0;
    }

}