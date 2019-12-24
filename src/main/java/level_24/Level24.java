package level_24;

import common.Level;
import common.Point3D;

import java.util.*;

public class Level24 extends Level {
    final private int W = 5;
    final private int H = 5;
    final static int DEFAULT_LAYER = 200;
    final static int STACK_DEPTH = 402; // Bugs can't get deeper in 200 steps
    final List<String> in;
    boolean[] recursiveMap;
    final private static boolean VERBOSE = false;

    public Level24(String filename) {
        in = readResources(filename);
        parseInput();
    }

    private void parseInput() {
        recursiveMap = new boolean[STACK_DEPTH * H * W];
        for (int y = 0; y < H; y++) {
            char[] s = in.get(y).toCharArray();
            for (int x = 0; x < W; x++) {
                if (s[x] == '#') {
                    recursiveMap[coordinatesToIdx(x, y, DEFAULT_LAYER)] = true;
                }
            }
        }
    }

    private int coordinatesToIdx(int x, int y, int z) {
        return x + y * W + z * W * H;
    }

    long p1() {
        Set<Long> seenDiversities = new HashSet<>();
        seenDiversities.add(getBiodiversity(DEFAULT_LAYER));
        for (; ; ) {
            boolean[] nextFrame = new boolean[recursiveMap.length];
            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    int currentIdx = coordinatesToIdx(x, y, DEFAULT_LAYER);
                    int n = countNeighborsP1(x, y);
                    if (recursiveMap[currentIdx]) {
                        nextFrame[currentIdx] = n == 1;
                    } else {
                        nextFrame[currentIdx] = (n == 1 || n == 2);
                    }
                }
            }
            recursiveMap = nextFrame;
            long bd = getBiodiversity(DEFAULT_LAYER);
            if (seenDiversities.contains(bd)) {
                return bd;
            }
            seenDiversities.add(bd);
        }
    }

    int p2(int steps) {
        int minZ = DEFAULT_LAYER - 1; // Limit scan area, 4 times speedup
        int maxZ = DEFAULT_LAYER + 1;
        for (int step = 0; step < steps; step++) {
            boolean[] nextFrame = new boolean[recursiveMap.length];

            if (VERBOSE) printAllDimensions();
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = 0; y < H; y++) {
                    for (int x = 0; x < W; x++) {
                        if (x == 2 && y == 2) {
                            continue;
                        }
                        int curIdx = coordinatesToIdx(x, y, z);
                        int n = countNeighborsMultiDim(x, y, z);
                        if (recursiveMap[coordinatesToIdx(x, y, z)]) {
                            nextFrame[curIdx] = n == 1;
                        } else {
                            nextFrame[curIdx] = (n == 1 || n == 2);
                        }
                    }
                }
            }
            recursiveMap = nextFrame;
            if (getBiodiversity(minZ) > 0) {
                minZ--;
            }
            if (getBiodiversity(maxZ) > 0) {
                maxZ++;
            }
        }
        int result = 0;
        for (boolean cell : recursiveMap) {
            result += bToI(cell);
        }
        return result;
    }

    private int countNeighborsP1(int x, int y) {
        int z = DEFAULT_LAYER;
        List<Point3D> n = new ArrayList<>();
        // Left
        if (x > 0) {
            addNeighbor(n, x - 1, y, z);
        }
        // Right
        if (x < 4) {
            addNeighbor(n, x + 1, y, z);
        }
        // Up
        if (y > 0) {
            addNeighbor(n, x, y - 1, z);
        }
        if (y < 4) {
            addNeighbor(n, x, y + 1, z);
        }
        return countAliveInCoordinates(n);
    }

    private int bToI(boolean b) {
        return b ? 1 : 0;
    }

    /*
   0 1 2 3 4
0  - - - - -
1  - # # # -
2  - # + # -
3  - # # # -
4  - - - - -
     */
    private int countNeighborsMultiDim(int x, int y, int z) {
        List<Point3D> n = new ArrayList<>();

        // Left
        if (x == 0) {
            addNeighbor(n, 1, 2, z - 1);
        } else if (x == 3 && y == 2) {
            addNeighbor(n, 4, 0, z + 1);
            addNeighbor(n, 4, 1, z + 1);
            addNeighbor(n, 4, 2, z + 1);
            addNeighbor(n, 4, 3, z + 1);
            addNeighbor(n, 4, 4, z + 1);
        } else {
            addNeighbor(n, x - 1, y, z);
        }
        // Right
        if (x == 4) {
            addNeighbor(n, 3, 2, z - 1);
        } else if (x == 1 && y == 2) {
            addNeighbor(n, 0, 0, z + 1);
            addNeighbor(n, 0, 1, z + 1);
            addNeighbor(n, 0, 2, z + 1);
            addNeighbor(n, 0, 3, z + 1);
            addNeighbor(n, 0, 4, z + 1);
        } else {
            addNeighbor(n, x + 1, y, z);
        }
        // Up
        if (y == 0) {
            addNeighbor(n, 2, 1, z - 1);
        } else if (x == 2 && y == 3) {
            addNeighbor(n, 0, 4, z + 1);
            addNeighbor(n, 1, 4, z + 1);
            addNeighbor(n, 2, 4, z + 1);
            addNeighbor(n, 3, 4, z + 1);
            addNeighbor(n, 4, 4, z + 1);
        } else {
            addNeighbor(n, x, y - 1, z);
        }
        // Down
        if (y == 4) {
            addNeighbor(n, 2, 3, z - 1);
        } else if (x == 2 && y == 1) {
            addNeighbor(n, 0, 0, z + 1);
            addNeighbor(n, 1, 0, z + 1);
            addNeighbor(n, 2, 0, z + 1);
            addNeighbor(n, 3, 0, z + 1);
            addNeighbor(n, 4, 0, z + 1);
        } else {
            addNeighbor(n, x, y + 1, z);
        }
        int alive = countAliveInCoordinates(n);
        if (VERBOSE) System.out.printf("[%d:%d:%d] alive: %d neighbors: %d n: %s%n", x, y, z, alive, n.size(), n);
        return alive;
    }

    private void addNeighbor(List<Point3D> neighbors, int x, int y, int z) {
        neighbors.add(new Point3D(x, y, z));
    }

    private void printAllDimensions() {
        for (int z = 0; z < STACK_DEPTH; z++) {
            if (getBiodiversity(z) == 0) {
                continue;
            }
            System.out.println("Layer " + (z - DEFAULT_LAYER));
            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    System.out.print(recursiveMap[coordinatesToIdx(x, y, z)] ? '#' : '.');
                }
                System.out.print(System.lineSeparator());
            }
        }
    }

    private int countAliveInCoordinates(Collection<Point3D> neighbors) {
        int result = 0;
        for (Point3D p : neighbors) {
            result += bToI(recursiveMap[coordinatesToIdx(p.getX(), p.getY(), p.getZ())]);
        }
        return result;
    }

    long getBiodiversity(int layer) {
        long result = 0;
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (recursiveMap[coordinatesToIdx(x, y, layer)]) {
                    int power = y * W + x;
                    result += 1 << power;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Level24 l = new Level24("input");
        System.out.println("Part1: " + l.p1());
        l.parseInput();
        System.out.println("Part2: " + l.p2(200));
    }
}
