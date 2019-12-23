package level_08;

import common.Level;

public class Level08 extends Level {
    int[] data;
    final int w;
    final int h;
    int layers;

    public Level08(String filename, int w, int h) {
        this.w = w;
        this.h = h;
        String s = readResourcesFirstLine(filename);
        this.data = parseData(s);
    }

    int[] parseData(String in) {
        String[] tokens = in.split("");
        int[] result = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            result[i] = Integer.parseInt(tokens[i]);
        }
        data = result;
        layers = result.length / (w * h);
        return result;
    }

    int p1() {
        int l = getLayerWithFewestZeros();
        int ones = 0;
        int twos = 0;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int d = getDigitAt(i, j, l);
                if (d == 1) {
                    ones++;
                } else if (d == 2) {
                    twos++;
                }
            }
        }

        return ones * twos;
    }

    String p2() {
        StringBuilder sb = new StringBuilder(System.lineSeparator());
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                layerLoop:
                for (int l = 0; l < layers; l++) {
                    int p = getDigitAt(j, i, l);
                    switch (p) {
                        case 0:
                            sb.append(' ');
                            break layerLoop;
                        case 1:
                            sb.append('#');
                            break layerLoop;
                    }
                }
                sb.append(' ');
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    int getLayerWithFewestZeros() {
        int fz = Integer.MAX_VALUE;
        int fli = -1;
        for (int l = 0; l < layers; l++) {
            int zc = 0;

            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int d = getDigitAt(i, j, l);
                    if (d == 0) {
                        zc++;
                    }
                }
            }

            if (zc < fz) {
                fz = zc;
                fli = l;
            }
        }
        return fli;
    }

    int getDigitAt(int x, int y, int z) {

        int idx = z * (w * h) + w * y + x;
        if (x < 0 || y < 0 || z < 0 || x >= w || y >= h || z >= layers || idx >= data.length || idx < 0) {
            throw new IndexOutOfBoundsException("No such coordinate in image");
        }
        return data[idx];
    }

    public static void main(String[] args) {
        Level08 l = new Level08("input", 25, 6);
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2());
    }

}
