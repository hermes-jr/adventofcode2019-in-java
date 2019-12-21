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
    Point2D currentPosition;
    int shortestSeenSoFar = Integer.MAX_VALUE;
    BidiMap<Character, Point2D> keys = new DualHashBidiMap<>();
    BidiMap<Character, Point2D> doors = new DualHashBidiMap<>();
    Map<Point2D, ShortestPathAlgorithm.SingleSourcePaths<Point2D, DefaultWeightedEdge>> shortPaths = new HashMap<>();
    SimpleGraph<Point2D, DefaultWeightedEdge> g = new SimpleGraph<>(DefaultWeightedEdge.class);
    Set<ImmutablePair<Point2D, Set<Point2D>>> cache = new HashSet<>();

    public Level18(String filename) {
        List<String> in = readResources(filename);
        parseMap(in);
        DijkstraShortestPath<Point2D, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<>(g);
        keys.values().forEach(z -> shortPaths.put(z, dijkstraAlg.getPaths(z)));
        shortPaths.put(currentPosition, dijkstraAlg.getPaths(currentPosition));
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
                    currentPosition = new Point2D(j, i);
                } else if (Character.isUpperCase(cc)) {
                    doors.put(cc, currentPoint);
                } else if (Character.isLowerCase(cc)) {
                    keys.put(cc, currentPoint);
                }
            }
        }

    }

    int p1() {
        List<Character> keysFound = Collections.emptyList();
        Set<Point2D> lockedDoors = Collections.unmodifiableSet(new HashSet<>(doors.values()));
        visitPoint(keysFound, lockedDoors, currentPosition, 0);
        return shortestSeenSoFar;
    }

    void visitPoint(List<Character> keysFound, Set<Point2D> lockedDoors, Point2D currentPoint, int stepsSoFar) {
//        System.out.println("Visiting " + currentPoint + ", keys found: " + keysFound);
        if (stepsSoFar >= shortestSeenSoFar) { // No reason to dig deeper
            return;
        }
        if (cache.contains(ImmutablePair.of(currentPoint, lockedDoors))) {
            return;
        }
        List<Character> nKeysFound = new ArrayList<>(keysFound);
        Character keyName = keys.getKey(currentPoint);
        Set<Point2D> nLockedDoors = new HashSet<>(lockedDoors);
        if (keyName != null) {
//            System.out.println("Collected key: " + keyName);
            nKeysFound.add(keyName);
            nLockedDoors.remove(doors.get(Character.toUpperCase(keyName)));
        }
        if (nKeysFound.size() == keys.size()) { // No more keys to collect
            if (stepsSoFar <= shortestSeenSoFar) {
                shortestSeenSoFar = stepsSoFar;
            }
            return;
        }

        Map<Point2D, Integer> reachableKeys = new HashMap<>();
        for (Point2D potentiallyReachable : keys.values()) {
            List<Point2D> path = shortPaths.get(currentPoint).getPath(potentiallyReachable).getVertexList();
            for (Point2D p : path) {
                if (nLockedDoors.contains(p)) {
                    break;
                }
                if (p.equals(potentiallyReachable) && !nKeysFound.contains(keys.getKey(p))) {
                    reachableKeys.put(p, path.size());
                }
            }
        }
        if (reachableKeys.size() == 0) {
            throw new RuntimeException("No reachable keys");
        }

        for (Map.Entry<Point2D, Integer> entry : reachableKeys.entrySet()) {
            Point2D nextStep = entry.getKey();
            visitPoint(Collections.unmodifiableList(nKeysFound), Collections.unmodifiableSet(nLockedDoors), nextStep, stepsSoFar + entry.getValue() - 1);
        }

        cache.add(ImmutablePair.of(currentPoint, Collections.unmodifiableSet(nLockedDoors)));
    }

    public static void main(String[] args) {
        Level18 l = new Level18("input");

        System.out.println("Part1: " + l.p1());
//        System.out.println("Part2: " + l.p2(g));
    }

}
