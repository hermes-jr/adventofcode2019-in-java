package level_02;

import common.Level;

import java.util.Arrays;

public class Level02 extends Level {

    int[] parseData(String in) {
        String[] tokens = in.split(",");
        int[] data = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Integer.parseInt(tokens[i]);
        }
        return data;
    }

    int p1(int[] data) {
        return runProg(data, 12, 2);
    }

    int p2(int[] data) {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (runProg(data, i, j) == 19690720) {
                    return 100 * i + j;
                }
            }
        }
        return -1;
    }

    int runProg(int[] ind, int a, int b) {
        // protect in data
        int[] data = Arrays.copyOf(ind, ind.length);

        data[1] = a;
        data[2] = b;
        int ip = 0;
        mainLoop:
        while (true) {
            int cv = data[ip];
            switch (cv) {
                case 99:
                    // halt code
                    break mainLoop;
                case 1:
                    data[data[ip + 3]] = data[data[ip + 1]] + data[data[ip + 2]];
                    break;
                case 2:
                    data[data[ip + 3]] = data[data[ip + 1]] * data[data[ip + 2]];
                    break;
            }
            ip += 4;
        }

        return data[0];
    }

    public static void main(String[] args) {
        Level02 l = new Level02();
        int[] data = l.parseData(l.readResourcesFirstLine("input"));
        System.out.println("Part1: " + l.p1(data));
        System.out.println("Part2: " + l.p2(data));
    }
}
