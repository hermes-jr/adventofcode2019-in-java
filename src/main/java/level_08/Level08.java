package level_08;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Level08 {
    int[] data;
    int w;
    int h;
    int layers;

    public Level08(String filename, int w, int h) {
        this.w = w;
        this.h = h;
        String s = readResources(filename);
        this.data = parseData(s);
    }

    int[] parseData(String indata) {
        String[] tokens = indata.split("");
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

    public String readResources(String filename) {
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_" + getClass().getSimpleName().substring(5) + "/" + filename))))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }

    int getDigitAt(int x, int y, int z) {
        int idx = z * (w + h + 1) + w * y + x;
        if (idx >= data.length) {
            throw new IndexOutOfBoundsException("No such coordinate in image");
        }
        return data[idx];
    }

    public static void main(String[] args) {
        Level08 l = new Level08("input", 25, 6);
        System.out.println("Part1: " + l.p1());
    }

}
