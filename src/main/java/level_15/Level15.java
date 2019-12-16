package level_15;

import common.IntComp;
import common.Level;
import common.Point2D;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

public class Level15 extends Level {
    static final int UP = 1;
    static final int DOWN = 2;
    static final int LEFT = 3;
    static final int RIGHT = 4;

    int oxDistanceFromStart = 0;
    int maxPathFromOx = 0;
    private Bot bot;
    private Set<Point2D> visited = new HashSet<>();
    private Set<Point2D> walls = new HashSet<>();
    boolean firstVisit = true;
    private final static boolean VERBOSE = false;

    public Level15(String input) {
        String program = readResourcesFirstLine(input);
        bot = new Bot(program);
    }

    int p1() {
        Stack<Point2D> path = new Stack<>();
        path.push(bot.getLocation());
        try {
            dfs(path, 1);
        } catch (InterruptedException e) {
            return oxDistanceFromStart;
        }
        return -1;
    }

    @SneakyThrows
    int p2() {
        Stack<Point2D> path = new Stack<>();
        visited.clear();
        walls.clear();
        path.push(bot.getLocation());
        firstVisit = true;
        dfs(path, 2);
        return maxPathFromOx;
    }

    /**
     * At each step bot pokes adjacent cells to scan for possible paths, marks walls as visited.
     * After that, it moves as deep as possible in each direction. For part 1 the loop is broken as
     * soon as oxygen found. Then visited points are cleared and the whole map is explored to find
     * the furthest location.
     *
     * @param stack Path from starting point to current location
     * @param part  Part of the puzzle, 1 or 2
     * @throws InterruptedException In part 1 exits the recursion when oxygen found.
     *                              It was easier to break recursion this way
     */
    void dfs(Stack<Point2D> stack, int part) throws InterruptedException {
        if (stack.isEmpty()) {
            if (VERBOSE) System.out.println("All points visited");
            return;
        }
        Point2D toExplore = stack.peek();
        if (part == 1 && bot.getOxygenLocation() != null && oxDistanceFromStart == 0) {
            oxDistanceFromStart = stack.size() - 1;
            throw new InterruptedException();
        } else if (part == 2 && stack.size() > maxPathFromOx) {
            maxPathFromOx = stack.size();
        }
        visited.add(toExplore); // Never re-visit this point unless returning
        if (VERBOSE) System.out.println("To explore: " + toExplore + ", stack: " + stack);
        if (!firstVisit) {
            bot.move(pointsToDirection(bot.location, toExplore));
            if (VERBOSE) renderScreen();
        }
        firstVisit = false;

        // Discover options first
        List<Point2D> adjacent = new ArrayList<>();
        markAsVisitedOrAddToAdjacent(adjacent, UP);
        markAsVisitedOrAddToAdjacent(adjacent, DOWN);
        markAsVisitedOrAddToAdjacent(adjacent, LEFT);
        markAsVisitedOrAddToAdjacent(adjacent, RIGHT);
        // Explore them
        for (Point2D np : adjacent) {
            stack.push(np);
            dfs(stack, part);
        }
        if (stack.size() > 1) {
            // Discard current point from path and move one step back
            stack.pop();
            bot.move(pointsToDirection(bot.location, stack.peek()));
            if (VERBOSE) renderScreen();
        }
    }

    private void markAsVisitedOrAddToAdjacent(List<Point2D> adjacent, int direction) {
        Point2D np = getNextPoint(bot.getLocation(), direction);
        if (visited.contains(np)) {
            return;
        }
        if (bot.poke(direction)) {
            adjacent.add(np);
        } else {
            walls.add(np);
            visited.add(np);
        }
    }

    private void renderScreen() {
        StringBuilder result = new StringBuilder(System.lineSeparator());
        Map<Point2D, Character> mapToRender = new HashMap<>();
        visited.forEach(z -> mapToRender.put(z, '.'));
        walls.forEach(z -> mapToRender.put(z, '#'));
        if (bot.getOxygenLocation() != null) {
            mapToRender.put(bot.getOxygenLocation(), 'o');
        }
        mapToRender.put(Point2D.ZERO, '*');
        if (!bot.getLocation().equals(bot.getOxygenLocation())) {
            mapToRender.put(bot.getLocation(), '@');
        } else {
            mapToRender.put(bot.getLocation(), '8');
        }
        IntSummaryStatistics xStats = mapToRender.keySet().stream().collect(Collectors.summarizingInt(Point2D::getX));
        IntSummaryStatistics yStats = mapToRender.keySet().stream().collect(Collectors.summarizingInt(Point2D::getY));
        for (int i = yStats.getMin(); i <= yStats.getMax(); i++) {
            for (int j = xStats.getMin(); j <= xStats.getMax(); j++) {
                result.append(mapToRender.getOrDefault(new Point2D(j, i), ' '));
            }
            result.append(System.lineSeparator());
        }
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        Level15 l = new Level15("input");
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2());
    }

    int inverse(int direction) {
        switch (direction) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }
        throw new RuntimeException("Unknown direction: " + direction);
    }

    private int pointsToDirection(Point2D from, Point2D to) {
        int result = 0;
        if (from.getY() == to.getY()) {
            if (from.getX() - 1 == to.getX()) {
                return LEFT;
            } else if (from.getX() + 1 == to.getX()) {
                return RIGHT;
            }
        } else if (from.getX() == to.getX()) {
            if (from.getY() - 1 == to.getY()) {
                return UP;
            } else if (from.getY() + 1 == to.getY()) {
                return DOWN;
            }
        }
        System.err.println("Impossible movement requested: " + from + " -> " + to);
        return result;
    }

    private Point2D getNextPoint(Point2D bot, int direction) {
        switch (direction) {
            case UP: // n
                return new Point2D(bot.getX(), bot.getY() - 1);
            case DOWN: // s
                return new Point2D(bot.getX(), bot.getY() + 1);
            case LEFT: // w
                return new Point2D(bot.getX() - 1, bot.getY());
            case RIGHT: // e
                return new Point2D(bot.getX() + 1, bot.getY());
            default:
                throw new RuntimeException("Unknown direction: " + direction);
        }
    }

    class Bot {
        IntComp ic;
        @Getter
        Point2D location = Point2D.ZERO;
        @Getter
        Point2D oxygenLocation;

        Bot(String program) {
            ic = new IntComp(program, 0);
        }

        /**
         * Investigate direction and return back if actually moved
         *
         * @param direction which direction to poke
         * @return false if wall, true if hallway
         */
        boolean poke(int direction) {
            boolean moved = move(direction);
            if (moved) {
                move(inverse(direction));
            }
            return moved;
        }

        /**
         * Move bot in required direction, reporting it movement was successful
         *
         * @param direction UP, DOWN, LEFT, RIGHT
         * @return true if moved, false if stumbled upon the wall
         */
        boolean move(int direction) {
            Point2D nextPossiblePoint = getNextPoint(location, direction);
            ic.addToInput(direction);
            ic.run();
            Queue<Long> out = ic.getOutput();
            int o = Objects.requireNonNull(out.poll()).intValue();
            switch (o) {
                case 0: // wall
                    return false;
                case 2: // moved, found oxygen
                    if (oxygenLocation == null) {
                        oxygenLocation = nextPossiblePoint;
                    }
                case 1: // moved
                    location = nextPossiblePoint;
                    return true;
                default:
                    throw new RuntimeException("Unexpected response from bot: " + o);
            }
        }
    }
}