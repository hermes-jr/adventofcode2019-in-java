package level_20;


import common.Level;
import common.Point2D;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Level20 extends Level {
    Point2D start;
    Point2D end;
    Map<String, Point2D> portals = new HashMap<>();

    SimpleGraph<Point2D, DefaultEdge> parseMap(List<String> indata) {
        SimpleGraph<Point2D, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        int w = indata.get(0).length();
        int h = indata.size();

        char[][] asChars = new char[h][w];
        for (int i = 0; i < h; i++) {
            asChars[i] = indata.get(i).toCharArray();
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (asChars[i][j] == '.') {
                    // This is a passage
                    Point2D currentPoint = new Point2D(j, i);
                    g.addVertex(currentPoint);

                    // Can we go up? If so, connect
                    if (asChars[i - 1][j] == '.') {
                        g.addEdge(currentPoint, new Point2D(j, i - 1));
                    }

                    // Can we go left? If so, connect
                    if (asChars[i][j - 1] == '.') {
                        g.addEdge(currentPoint, new Point2D(j - 1, i));
                    }

                    // Is the current location named?
                    Optional<String> currentPointNameO = getNameForPoint(asChars, currentPoint);
                    if (currentPointNameO.isEmpty()) {
                        continue;
                    }
                    String currentPointName = currentPointNameO.get();

                    if ("AA".equals(currentPointName)) {
                        start = currentPoint;
                    } else if ("ZZ".equals(currentPointName)) {
                        end = currentPoint;
                    }

                    // Is the other side already discovered?
                    Point2D otherSide = portals.get(currentPointName);
                    if (otherSide != null) {
                        g.addEdge(currentPoint, otherSide);
                    } else {
                        portals.put(currentPointName, currentPoint);
                    }
                }
            }
        }

        return g;
    }

    private Optional<String> getNameForPoint(char[][] asChars, Point2D currentCoordinate) {
        int x = currentCoordinate.getX();
        int y = currentCoordinate.getY();

        // Sign above
        if (isLetter(asChars[y - 1][x])) {
            return Optional.of(String.valueOf(asChars[y - 2][x]) + asChars[y - 1][x]);
        }
        // Sign on the left
        if (isLetter(asChars[y][x - 1])) {
            return Optional.of(String.valueOf(asChars[y][x - 2]) + asChars[y][x - 1]);
        }
        // Sign on the right
        if (isLetter(asChars[y][x + 1])) {
            return Optional.of(String.valueOf(asChars[y][x + 1]) + asChars[y][x + 2]);
        }
        // Sign below
        if (isLetter(asChars[y + 1][x])) {
            return Optional.of(String.valueOf(asChars[y + 1][x]) + asChars[y + 2][x]);
        }
        return Optional.empty();
    }

    private boolean isLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    int p1(SimpleGraph<Point2D, DefaultEdge> g) {
        DijkstraShortestPath<Point2D, DefaultEdge> dijkstraAlg =
                new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<Point2D, DefaultEdge> iPaths = dijkstraAlg.getPaths(start);
        GraphPath<Point2D, DefaultEdge> toExit = iPaths.getPath(end);
        return toExit.getLength();
    }

    public static void main(String[] args) {
        Level20 l = new Level20();
        SimpleGraph<Point2D, DefaultEdge> g = l.parseMap(l.readResources("input"));


        System.out.println("Part1: " + l.p1(g));
    }

}
