package level_24;

import common.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level24 extends Level {
    boolean[][] map;
    int w;
    int h;

    public Level24(String filename) {
        List<String> in = readResources(filename);
        h = in.size();
        w = in.get(0).length();
        map = new boolean[h + 2][w + 2];
        for (int i = 0; i < h; i++) {
            char[] s = in.get(i).toCharArray();
            for (int j = 0; j < w; j++) {
                if (s[j] == '#') {
                    map[i + 1][j + 1] = true;
                }
            }
        }
    }

    long p1() {
        Set<Long> seenDiversities = new HashSet<>();
        seenDiversities.add(getBiodiversity());
        for (; ; ) {
            boolean[][] nextFrame = new boolean[h + 2][w + 2];
            for (int i = 1; i < h + 1; i++) {
                for (int j = 1; j < w + 1; j++) {
                    int n = countNeighbors(i, j);
                    if (map[i][j]) {
                        nextFrame[i][j] = n == 1;
                    } else {
                        nextFrame[i][j] = (n == 1 || n == 2);
                    }
                }
            }
            map = nextFrame;
            long bd = getBiodiversity();
            if (seenDiversities.contains(bd)) {
                return bd;
            }
            seenDiversities.add(bd);
        }
    }

    private int countNeighbors(int i, int j) {
        int result = 0;
        if (map[i - 1][j]) {
            result++;
        }
        if (map[i + 1][j]) {
            result++;
        }
        if (map[i][j - 1]) {
            result++;
        }
        if (map[i][j + 1]) {
            result++;
        }
        return result;
    }

    long getBiodiversity() {
        long result = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (map[i + 1][j + 1]) {
                    int power = i * w + j;
                    result += 1 << power;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Level24 l = new Level24("input");
        System.out.println("Part1: " + l.p1());
    }
}
