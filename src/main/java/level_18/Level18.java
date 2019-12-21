package level_18;


import common.Level;
import common.Point2D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

public class Level18 extends Level {
    SimpleGraph<Point2D, DefaultWeightedEdge> g = new SimpleGraph<>(DefaultWeightedEdge.class);
    Point2D startingPosition;
    Set<Point2D> keys = new HashSet<>();
    Set<Point2D> doors = new HashSet<>();
    Map<Point2D, Point2D> doorToKey = new HashMap<>();
    Map<ImmutablePair<Point2D, Set<Point2D>>, Integer> cache = new HashMap<>();
    Map<ImmutablePair<Point2D, Point2D>, List<Point2D>> pathsCache = new HashMap<>();

    public Level18(String filename) {
        List<String> in = readResources(filename);
        parseMap(in);
        ShortestPathAlgorithm<Point2D, DefaultWeightedEdge> shortestPathAlg = new DijkstraShortestPath<>(g);
        keys.forEach(
                k -> {
                    ShortestPathAlgorithm.SingleSourcePaths<Point2D, DefaultWeightedEdge> alg = shortestPathAlg.getPaths(k);
                    keys.forEach(
                            sk -> {
                                if (!sk.equals(k)) {
                                    pathsCache.put(ImmutablePair.of(k, sk), alg.getPath(sk).getVertexList());
                                }
                            }
                    );
                }
        );
        ShortestPathAlgorithm.SingleSourcePaths<Point2D, DefaultWeightedEdge> alg = shortestPathAlg.getPaths(startingPosition);
        keys.forEach(
                k -> {
                    pathsCache.put(ImmutablePair.of(startingPosition, k), alg.getPath(k).getVertexList());
                    pathsCache.put(ImmutablePair.of(k, k), Collections.emptyList());
                }
        );
        pathsCache.put(ImmutablePair.of(startingPosition, startingPosition), Collections.emptyList());
    }

    void parseMap(List<String> indata) {
        Point2D[] ks = new Point2D[32];
        Point2D[] ds = new Point2D[32];
        int w = indata.get(0).length();
        int h = indata.size();

        char[][] asChars = new char[h][w];
        for (int i = 0; i < h; i++) {
            asChars[i] = indata.get(i).toCharArray();
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                char cc = asChars[i][j];
                if (cc == '#') {
                    continue;
                }
                Point2D currentPoint = new Point2D(j, i);
                g.addVertex(currentPoint);

                // Can we go up? If so, connect
                if (g.containsVertex(new Point2D(j - 1, i))) {
                    g.addEdge(currentPoint, new Point2D(j - 1, i));
                }

                // Can we go left? If so, connect
                if (g.containsVertex(new Point2D(j, i - 1))) {
                    g.addEdge(currentPoint, new Point2D(j, i - 1));
                }

                if (cc == '@') {
                    startingPosition = new Point2D(j, i);
                } else if (Character.isUpperCase(cc)) {
                    doors.add(currentPoint);
                    ds[cc - 65] = currentPoint;
                } else if (Character.isLowerCase(cc)) {
                    keys.add(currentPoint);
                    ks[cc - 97] = currentPoint;
                }
            }
        }

        for (int i = 0; i < 32; i++) {
            if (ks[i] != null) {
                doorToKey.put(ds[i], ks[i]);
            }
        }

    }

    int p1() {
        return recursiveVisitPoint(keys, startingPosition);
    }

    int recursiveVisitPoint(Set<Point2D> keysLeft, Point2D currentPoint) {
        if (keysLeft.isEmpty()) {
            return 0;
        }

        ImmutablePair<Point2D, Set<Point2D>> keyPath = ImmutablePair.of(currentPoint, Collections.unmodifiableSet(keysLeft));
        Integer cachedDistance = cache.get(keyPath);
        if (cachedDistance != null) {
            return cachedDistance;
        }

        int result = Integer.MAX_VALUE;

        Map<Point2D, Integer> reachableKeys = getReachableKeys(keysLeft, currentPoint);

        for (Map.Entry<Point2D, Integer> entry : reachableKeys.entrySet()) {
            Set<Point2D> nextKeysLeft = new HashSet<>(keysLeft);
            nextKeysLeft.remove(currentPoint);
            int selfDistance = entry.getValue();
            int distanceToEnd = recursiveVisitPoint(Collections.unmodifiableSet(nextKeysLeft), entry.getKey());
            result = Math.min(selfDistance + distanceToEnd, result);
        }

        cache.put(keyPath, result);
        return result;
    }

    private Map<Point2D, Integer> getReachableKeys(Set<Point2D> keysLeft, Point2D currentPoint) {
        Map<Point2D, Integer> reachableKeys = new HashMap<>();
        outerLoop:
        for (Point2D potentiallyReachable : keysLeft) {
            List<Point2D> path = pathsCache.get(ImmutablePair.of(currentPoint, potentiallyReachable));
            for (Point2D p : path) {
                if (doors.contains(p) && keysLeft.contains(doorToKey.get(p))) {
                    continue outerLoop;
                }
            }
            reachableKeys.put(potentiallyReachable, Math.max(0, path.size() - 1));
        }
        if (reachableKeys.size() == 0) {
            throw new RuntimeException("No reachable keys");
        }
        return reachableKeys;
    }

    public static void main(String[] args) {
        Level18 l = new Level18("input");

        System.out.println("Part1: " + l.p1());
//        System.out.println("Part2: " + l.p2(g));
    }

}
