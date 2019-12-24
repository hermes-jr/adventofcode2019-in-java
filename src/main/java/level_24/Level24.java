package level_24;

import common.Level;
import common.Point3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Level24 extends Level {
    boolean[] recursiveMap;
    final static int DEFAULT_LAYER = 200;
    final static int STACK_DEPTH = 402; // Bugs can't get deeper in 200 steps
    final private static boolean VERBOSE = false;
    int w;
    int h;

    public Level24(String filename) {
        List<String> in = readResources(filename);
        h = in.size();
        w = in.get(0).length();
        recursiveMap = new boolean[STACK_DEPTH * h * w];
        for (int y = 0; y < h; y++) {
            char[] s = in.get(y).toCharArray();
            for (int x = 0; x < w; x++) {
                if (s[x] == '#') {
                    recursiveMap[coordToIdx(x, y, DEFAULT_LAYER)] = true;
                }
            }
        }
    }

    private int coordToIdx(int x, int y, int z) {
        return x + y * w + z * w * h;
    }

    int p2(int steps) {
        for (int step = 0; step < steps; step++) {
            boolean[] nextFrame = new boolean[recursiveMap.length];

            if (VERBOSE) printAllDimensions();
            for (int z = 1; z < STACK_DEPTH - 1; z++) {
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (x == 2 && y == 2) {
                            continue;
                        }
                        int curIdx = coordToIdx(x, y, z);
                        int n = countNeighborsMultiDim(x, y, z);
                        if (recursiveMap[coordToIdx(x, y, z)]) {
                            nextFrame[curIdx] = n == 1;
                        } else {
                            nextFrame[curIdx] = (n == 1 || n == 2);
                        }
                    }
                }
            }
            recursiveMap = nextFrame;
        }
        int result = 0;
        for (boolean cell : recursiveMap) {
            result += bToI(cell);
        }
        return result;
    }

    private int countNeighborsP1(boolean[] data, int x, int y) {
        int result = 0;
        List<Point3D> neighbors = new ArrayList<>();
        // Left
        if (x > 0) {
            result += bToI(data[coordToIdx(x - 1, y, DEFAULT_LAYER)]);
        }
        // Right
        if (x < 4) {
            result += bToI(data[coordToIdx(x + 1, y, DEFAULT_LAYER)]);
        }
        // Up
        if (y > 0) {
            result += bToI(data[coordToIdx(x, y - 1, DEFAULT_LAYER)]);
        }
        if (y < 4) {
            result += bToI(data[coordToIdx(x, y + 1, DEFAULT_LAYER)]);
        }
        return result;
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
        List<Point3D> neighbors = new ArrayList<>();

        // Left
        if (x == 0) {
            neighbors.add(new Point3D(1, 2, z - 1));
        } else if (x == 3 && y == 2) {
            neighbors.add(new Point3D(4, 0, z + 1));
            neighbors.add(new Point3D(4, 1, z + 1));
            neighbors.add(new Point3D(4, 2, z + 1));
            neighbors.add(new Point3D(4, 3, z + 1));
            neighbors.add(new Point3D(4, 4, z + 1));
        } else {
            neighbors.add(new Point3D(x - 1, y, z));
        }
        // Right
        if (x == 4) {
            neighbors.add(new Point3D(3, 2, z - 1));
        } else if (x == 1 && y == 2) {
            neighbors.add(new Point3D(0, 0, z + 1));
            neighbors.add(new Point3D(0, 1, z + 1));
            neighbors.add(new Point3D(0, 2, z + 1));
            neighbors.add(new Point3D(0, 3, z + 1));
            neighbors.add(new Point3D(0, 4, z + 1));
        } else {
            neighbors.add(new Point3D(x + 1, y, z));
        }
        // Up
        if (y == 0) {
            neighbors.add(new Point3D(2, 1, z - 1));
        } else if (x == 2 && y == 3) {
            neighbors.add(new Point3D(0, 4, z + 1));
            neighbors.add(new Point3D(1, 4, z + 1));
            neighbors.add(new Point3D(2, 4, z + 1));
            neighbors.add(new Point3D(3, 4, z + 1));
            neighbors.add(new Point3D(4, 4, z + 1));
        } else {
            neighbors.add(new Point3D(x, y - 1, z));
        }
        // Down
        if (y == 4) {
            neighbors.add(new Point3D(2, 3, z - 1));
        } else if (x == 2 && y == 1) {
            neighbors.add(new Point3D(0, 0, z + 1));
            neighbors.add(new Point3D(1, 0, z + 1));
            neighbors.add(new Point3D(2, 0, z + 1));
            neighbors.add(new Point3D(3, 0, z + 1));
            neighbors.add(new Point3D(4, 0, z + 1));
        } else {
            neighbors.add(new Point3D(x, y + 1, z));
        }
        int result = sumByGivenCoordinates(neighbors);
        if (VERBOSE) {
            System.out.printf("[%d:%d:%d]", x, y, z);
            System.out.print(" neighbors: " + neighbors.size());
            System.out.println("\tcount: " + result);
        }
        return result;
    }

    private void printAllDimensions() {
        for (int z = 0; z < STACK_DEPTH; z++) {
            System.out.println("Layer " + (z - DEFAULT_LAYER));
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    System.out.print(recursiveMap[coordToIdx(x, y, z)] ? '#' : '.');
                }
                System.out.print(System.lineSeparator());
            }
        }
    }

    private int sumByGivenCoordinates(Collection<Point3D> neighbors) {
        int result = 0;
        for (Point3D p : neighbors) {
            result += bToI(recursiveMap[coordToIdx(p.getX(), p.getY(), p.getZ())]);
        }
        return result;
    }

    long getBiodiversity(boolean[] slice, int layer) {
        long result = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (slice[coordToIdx(x, y, layer)]) {
                    int power = y * w + x;
                    result += 1 << power;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Level24 l = new Level24("input");
//        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2(200));
    }
}
