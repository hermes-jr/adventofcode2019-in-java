package level_10;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Level10 {
    final static boolean VERBOSE = false;
    List<Point> asteroids;
    SortedMap<BigDecimal, List<Point>> lines = new TreeMap<>();
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

    int p2(Point vantagePoint) {
        Point result = new Point(0, 0);
        getSinglePointVisibleCount(vantagePoint, true); // update lines
        System.out.println(lines);
        for (int i = 0; i < 200; i++) {
//            result = getNextEliminated();
        }
        return result.getX() * 100 + result.getY();
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
        int[] r1 = l.p1();
        System.out.println("Part1: " + r1[2]);
        System.out.println("Part2: " + l.p2(new Point(r1[0], r1[1])));
    }

    int getSinglePointVisibleCount(Point asteroid) {
        return getSinglePointVisibleCount(asteroid, false);
    }

    int getSinglePointVisibleCount(Point asteroid, boolean updateLines) {
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
            int x1 = asteroid.getX();
            int x2 = other.getX();
            int y1 = asteroid.getY();
            int y2 = other.getY();

            int a = y1 - y2;
            int b = x2 - x1;
            int c = x1 * y2 - x2 * y1;
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
            if (updateLines) {
//                BigDecimal angle = BigDecimal.valueOf(Math.toDegrees(Math.atan2(y2 - y1, x2 - x1) + 1.5 * Math.PI) % 360.0).setScale(6, RoundingMode.HALF_UP);
                double zz = Math.atan2(y2 - y1, x2 - x1) + 0.5 * Math.PI;
                if (zz > Math.PI) zz -= 2.0 * Math.PI;
                else if (zz < -Math.PI) zz += 2.0 * Math.PI;
                BigDecimal angle = BigDecimal.valueOf(Math.toDegrees(zz)).setScale(6, RoundingMode.HALF_UP);

                lines.put(angle, sameLine);
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


/*
--- Part Two ---

Once you give them the coordinates, the Elves quickly deploy an Instant Monitoring Station to the location and discover the worst: there are simply too many asteroids.

The only solution is complete vaporization by giant laser.

Fortunately, in addition to an asteroid scanner, the new monitoring station also comes equipped with a giant rotating laser perfect for vaporizing asteroids. The laser starts by pointing up and always rotates clockwise, vaporizing any asteroid it hits.

If multiple asteroids are exactly in line with the station, the laser only has enough power to vaporize one of them before continuing its rotation. In other words, the same asteroids that can be detected can be vaporized, but if vaporizing one asteroid makes another one detectable, the newly-detected asteroid won't be vaporized until the laser has returned to the same position by rotating a full 360 degrees.

For example, consider the following map, where the asteroid with the new monitoring station (and laser) is marked X:

.#....#####...#..
##...##.#####..##
##...#...#.#####.
..#.....X...###..
..#.#.....#....##

The first nine asteroids to get vaporized, in order, would be:

.#....###24...#..
##...##.13#67..9#
##...#...5.8####.
..#.....X...###..
..#.#.....#....##

Note that some asteroids (the ones behind the asteroids marked 1, 5, and 7) won't have a chance to be vaporized until the next full rotation. The laser continues rotating; the next nine to be vaporized are:

.#....###.....#..
##...##...#.....#
##...#......1234.
..#.....X...5##..
..#.9.....8....76

The next nine to be vaporized are then:

.8....###.....#..
56...9#...#.....#
34...7...........
..2.....X....##..
..1..............

Finally, the laser completes its first full rotation (1 through 3), a second rotation (4 through 8), and vaporizes the last asteroid (9) partway through its third rotation:

......234.....6..
......1...5.....7
.................
........X....89..
.................

In the large example above (the one with the best monitoring station location at 11,13):

    The 1st asteroid to be vaporized is at 11,12.
    The 2nd asteroid to be vaporized is at 12,1.
    The 3rd asteroid to be vaporized is at 12,2.
    The 10th asteroid to be vaporized is at 12,8.
    The 20th asteroid to be vaporized is at 16,0.
    The 50th asteroid to be vaporized is at 16,9.
    The 100th asteroid to be vaporized is at 10,16.
    The 199th asteroid to be vaporized is at 9,6.
    The 200th asteroid to be vaporized is at 8,2.
    The 201st asteroid to be vaporized is at 10,9.
    The 299th and final asteroid to be vaporized is at 11,1.

The Elves are placing bets on which will be the 200th asteroid to be vaporized. Win the bet by determining which asteroid that will be; what do you get if you multiply its X coordinate by 100 and then add its Y coordinate? (For example, 8,2 becomes 802.)

 */