package level_20;


import common.Level;
import common.Point3D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Level20 extends Level {
    Point3D start;
    Point3D end;
    Map<String, Point3D> innerPortals = new HashMap<>();
    Map<String, Point3D> outerPortals = new HashMap<>();

    SimpleGraph<Point3D, DefaultEdge> parseMap(List<String> indata) {
        SimpleGraph<Point3D, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

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
                    Point3D currentPoint = new Point3D(j, i, 0);
                    g.addVertex(currentPoint);

                    // Can we go up? If so, connect
                    if (asChars[i - 1][j] == '.') {
                        g.addEdge(currentPoint, new Point3D(j, i - 1, 0));
                    }

                    // Can we go left? If so, connect
                    if (asChars[i][j - 1] == '.') {
                        g.addEdge(currentPoint, new Point3D(j - 1, i, 0));
                    }

                    // Is the current location named?
                    Optional<ImmutablePair<String, Boolean>> pointNamingData = getNameForPoint(asChars, currentPoint);
                    if (pointNamingData.isEmpty()) {
                        continue;
                    }
                    String portName = pointNamingData.get().getLeft();
                    boolean portalOuter = pointNamingData.get().getRight();

                    if ("AA".equals(portName)) {
                        start = currentPoint;
                        continue;
                    } else if ("ZZ".equals(portName)) {
                        end = currentPoint;
                        continue;
                    }

                    if (portalOuter) {
                        outerPortals.put(portName, currentPoint);
                    } else {
                        innerPortals.put(portName, currentPoint);
                    }

                    // Is the other side already discovered?
                    Point3D otherSide = portalOuter ? innerPortals.get(portName) : outerPortals.get(portName);
                    if (otherSide != null) {
                        g.addEdge(currentPoint, otherSide);
                    }
                }
            }
        }

        return g;
    }

    private Optional<ImmutablePair<String, Boolean>> getNameForPoint(char[][] asChars, Point3D currentCoordinate) {
        int x = currentCoordinate.getX();
        int y = currentCoordinate.getY();
        boolean outer = false;
        if (x == 2 || y == 2 || x + 3 >= asChars[0].length || y + 3 >= asChars.length) {
            // On the edge
            outer = true;
        }

        // Signed above
        if (isLetter(asChars[y - 1][x])) {
            return Optional.of(ImmutablePair.of(String.valueOf(asChars[y - 2][x]) + asChars[y - 1][x], outer));
        }
        // Signed on the left
        else if (isLetter(asChars[y][x - 1])) {
            return Optional.of(ImmutablePair.of(String.valueOf(asChars[y][x - 2]) + asChars[y][x - 1], outer));
        }
        // Signed on the right
        else if (isLetter(asChars[y][x + 1])) {
            return Optional.of(ImmutablePair.of(String.valueOf(asChars[y][x + 1]) + asChars[y][x + 2], outer));
        }
        // Signed below
        else if (isLetter(asChars[y + 1][x])) {
            return Optional.of(ImmutablePair.of(String.valueOf(asChars[y + 1][x]) + asChars[y + 2][x], outer));
        }
        return Optional.empty();
    }

    private boolean isLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    int p1(SimpleGraph<Point3D, DefaultEdge> g) {
        return getShortestPath(g);
    }

    private int p2(SimpleGraph<Point3D, DefaultEdge> g) {
        // Remove all existing inter-portal links
        innerPortals.forEach((key, value) -> g.removeEdge(value, outerPortals.get(key)));

        //noinspection unchecked
        SimpleGraph<Point3D, DefaultEdge> templateLevel = (SimpleGraph<Point3D, DefaultEdge>) g.clone();
        templateLevel.removeVertex(start);
        templateLevel.removeVertex(end);

        for (int depth = 1; depth < 15; depth++) {
            final int efd = depth;
            // Copy template level into original graph preserving connectivity
            Graphs.addAllVertices(g,
                    templateLevel.vertexSet().stream()
                            .map(vtx -> new Point3D(vtx.getX(), vtx.getY(), efd))
                            .collect(Collectors.toList()));

            for (Point3D vtx : templateLevel.vertexSet()) {
                Graphs.successorListOf(templateLevel, vtx)
                        .forEach(
                                p2 -> g.addEdge(
                                        new Point3D(vtx.getX(), vtx.getY(), efd),
                                        new Point3D(p2.getX(), p2.getY(), efd))
                        );
            }

            // Add inter-level links
            for (Map.Entry<String, Point3D> innerPortalEntry : innerPortals.entrySet()) {
                Point3D outerPortal = outerPortals.get(innerPortalEntry.getKey());
                Point3D innerPortal = innerPortalEntry.getValue();
                g.addEdge(
                        new Point3D(innerPortal.getX(), innerPortal.getY(), efd - 1),
                        new Point3D(outerPortal.getX(), outerPortal.getY(), efd)
                );
            }
        }

        return getShortestPath(g);
    }

    private int getShortestPath(SimpleGraph<Point3D, DefaultEdge> g) {
        DijkstraShortestPath<Point3D, DefaultEdge> dijkstraAlg =
                new DijkstraShortestPath<>(g);
        ShortestPathAlgorithm.SingleSourcePaths<Point3D, DefaultEdge> iPaths = dijkstraAlg.getPaths(start);
        GraphPath<Point3D, DefaultEdge> toExit = iPaths.getPath(end);
        return toExit.getLength();
    }

    public static void main(String[] args) {
        Level20 l = new Level20();
        SimpleGraph<Point3D, DefaultEdge> g = l.parseMap(l.readResources("input"));

        System.out.println("Part1: " + l.p1(g));
        System.out.println("Part2: " + l.p2(g));
    }

}
