package level_18;


import common.Level;
import common.Point2D;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

public class Level18 extends Level {
    SimpleGraph<Point2D, DefaultWeightedEdge> g = new SimpleGraph<>(DefaultWeightedEdge.class);
    Point2D startingPosition;
    BidiMap<Character, Point2D> keys = new DualHashBidiMap<>();
    BidiMap<Character, Point2D> doors = new DualHashBidiMap<>();
    Map<Point2D, ShortestPathAlgorithm.SingleSourcePaths<Point2D, DefaultWeightedEdge>> shortPaths = new HashMap<>();
    Map<ImmutablePair<Point2D, Set<Point2D>>, Integer> cache = new HashMap<>();

    public Level18(String filename) {
        List<String> in = readResources(filename);
        parseMap(in);
        DijkstraShortestPath<Point2D, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<>(g);
        keys.values().forEach(z -> shortPaths.put(z, dijkstraAlg.getPaths(z)));
        shortPaths.put(startingPosition, dijkstraAlg.getPaths(startingPosition));
    }

    void parseMap(List<String> indata) {
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
                    doors.put(cc, currentPoint);
                } else if (Character.isLowerCase(cc)) {
                    keys.put(cc, currentPoint);
                }
            }
        }

    }

    int p1() {
        return recursiveVisitPoint(new HashSet<>(keys.values()), startingPosition);
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

        Set<Point2D> reachableKeys = getReachableKeys(keysLeft, currentPoint);

        for (Point2D nextKey : reachableKeys) {
            Set<Point2D> nextKeysLeft = new HashSet<>(keysLeft);
            nextKeysLeft.remove(currentPoint);
            int selfDistance = shortPaths.get(currentPoint).getPath(nextKey).getLength();
            int distanceToEnd = recursiveVisitPoint(Collections.unmodifiableSet(nextKeysLeft), nextKey);
            result = Math.min(selfDistance + distanceToEnd, result);
        }

        cache.put(keyPath, result);
        return result;
    }

    private Set<Point2D> getReachableKeys(Set<Point2D> keysLeft, Point2D currentPoint) {
        Set<Point2D> reachableKeys = new HashSet<>();
        for (Point2D potentiallyReachable : keysLeft) {
            List<Point2D> path = shortPaths.get(currentPoint).getPath(potentiallyReachable).getVertexList();
            for (Point2D p : path) {
                if (doors.containsValue(p)) { // It's a door
                    Point2D fittingKey = keys.get(Character.toLowerCase(doors.getKey(p)));
                    if (keysLeft.contains(fittingKey)) {
                        break; // Nothing to open with
                    }
                }
                if (p.equals(potentiallyReachable)) {
                    reachableKeys.add(p);
                }
            }
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