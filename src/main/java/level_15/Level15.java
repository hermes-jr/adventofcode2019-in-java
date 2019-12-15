package level_15;

import common.IntComp;
import common.Level;
import common.Point2D;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;
import java.util.stream.Collectors;

public class Level15 extends Level {
    IntComp ic;
    SimpleGraph<Point2D, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
    Point2D bot = Point2D.ZERO;
    Point2D oxygen;
    private final boolean VERBOSE = false;

    public Level15(String input) {
        String prog = readResourcesFirstLine(input);
        ic = new IntComp(prog, 0);
    }

    public int p1() {
        DijkstraShortestPath<Point2D, DefaultEdge> dijkstraAlg =
                new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<Point2D, DefaultEdge> iPath = dijkstraAlg.getPaths(oxygen);
        return iPath.getPath(Point2D.ZERO).getLength();
    }

    void scan() {
        g.addVertex(Point2D.ZERO);
        Random rnd = new Random();

        while (oxygen == null) {
            int direction = rnd.nextInt(4) + 1;
            Point2D nextPossiblePoint = getNextPoint(bot, direction);
            ic.addToInput(direction);
            ic.run();
            Queue<Long> out = ic.getOutput();
            int o = Objects.requireNonNull(out.poll()).intValue();
            switch (o) {
                case 0: // wall
                    break;
                case 2: // moved, found oxygen
                    oxygen = nextPossiblePoint;
                case 1: // moved
                    g.addVertex(nextPossiblePoint);
                    g.addEdge(bot, nextPossiblePoint);
                    bot = nextPossiblePoint;
                    break;
                default:
                    throw new RuntimeException("Unexpected response from bot");
            }

//            renderScreen();
        }
    }

    private Point2D getNextPoint(Point2D bot, int direction) {
        switch (direction) {
            case 1: // n
                return new Point2D(bot.getX(), bot.getY() - 1);
            case 2: // s
                return new Point2D(bot.getX(), bot.getY() + 1);
            case 3: // w
                return new Point2D(bot.getX() - 1, bot.getY());
            case 4: // e
                return new Point2D(bot.getX() + 1, bot.getY());
            default:
                throw new RuntimeException("Unknown direction");
        }
    }

    private void renderScreen() {
        StringBuilder result = new StringBuilder(System.lineSeparator());
        Map<Point2D, Character> renderableMap = new HashMap<>();
        g.vertexSet().forEach(z -> renderableMap.put(z, '.'));
        if (oxygen != null) {
            renderableMap.put(oxygen, 'o');
        }
        renderableMap.put(Point2D.ZERO, '*');
        if (!bot.equals(oxygen)) {
            renderableMap.put(bot, '@');
        } else {
            renderableMap.put(bot, '8');
        }
        IntSummaryStatistics xStats = renderableMap.keySet().stream().collect(Collectors.summarizingInt(Point2D::getX));
        IntSummaryStatistics yStats = renderableMap.keySet().stream().collect(Collectors.summarizingInt(Point2D::getY));
        for (int i = yStats.getMin(); i <= yStats.getMax(); i++) {
            for (int j = xStats.getMin(); j <= xStats.getMax(); j++) {
                result.append(renderableMap.getOrDefault(new Point2D(j, i), '#'));
            }
            result.append(System.lineSeparator());
        }
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        Level15 l = new Level15("input");
        l.scan();
        System.out.println("Part1: " + l.p1());
    }

}