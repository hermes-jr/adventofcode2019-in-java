package level_02;

import java.util.Arrays;

public class Level02 {

    int[] parseData(String indata) {
        String[] tokens = indata.split(",");
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
        int[] data = l.parseData("1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,19,9,23,1,5,23,27,1,27,9,31,1,6,31,35,2,35,9,39,1,39,6,43,2,9,43,47,1,47,6,51,2,51,9,55,1,5,55,59,2,59,6,63,1,9,63,67,1,67,10,71,1,71,13,75,2,13,75,79,1,6,79,83,2,9,83,87,1,87,6,91,2,10,91,95,2,13,95,99,1,9,99,103,1,5,103,107,2,9,107,111,1,111,5,115,1,115,5,119,1,10,119,123,1,13,123,127,1,2,127,131,1,131,13,0,99,2,14,0,0");
        System.out.println("Part1: " + l.p1(data));
        System.out.println("Part2: " + l.p2(data));
    }
}
