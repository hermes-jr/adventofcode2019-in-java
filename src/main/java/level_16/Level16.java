package level_16;

import common.Level;

import java.util.Arrays;

public class Level16 extends Level {
    final static int[] iv = new int[]{0, 1, 0, -1};

    public Level16() {
    }

    int[] parseString(String input) {
        char[] chars = input.toCharArray();
        int[] result = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            result[i] = chars[i] - 48;
        }
        return result;
    }

    public String p1(int[] data) {
        fft(data, 100);
        return getWithOffset(data, 0);
    }

    private String getWithOffset(int[] data, int offset) {
        StringBuilder result = new StringBuilder();
        for (int i = offset; i < 8 + offset; i++) {
            result.append(data[i]);
        }
        return result.toString();
    }

    public String p2(int[] data, int offset) {
        int[] bd = new int[data.length * 10000];
        for (int n = 0; n < 10_000 * data.length; n++) {
            bd[n] = data[n % data.length];
        }

        fft(bd, 100);
        return getWithOffset(bd, offset);
    }

    void fft(int[] data, int round) {
        if (round == 0) {
            return;
        }
        int[] prevState = Arrays.copyOf(data, data.length);
        // transform
        for (int i = 0; i < data.length; i++) {
            data[i] = getElement(prevState, i);
        }
//        System.out.println("ROUND: " + round);
//        System.out.println(Arrays.toString(data));

        fft(data, --round);
    }

    private int getElement(int[] prevState, int elNum) {
        elNum++;
        long sum = 0L;
        for (int i = 0; i < prevState.length; i++) {
            sum += prevState[i] * getMultiplier(i + 1, elNum);
        }
        return (int) Math.abs(sum % 10);
    }

    int getMultiplier(int curCycleIdx, int reps) {
        int i = curCycleIdx % (reps * iv.length);
        return iv[i / reps];
    }

    public static void main(String[] args) {
        Level16 l = new Level16();

        String in = l.readResourcesFirstLine("input");
        int offset = Integer.parseInt(in.substring(0, 7));
        System.out.println("Part1: " + l.p1(l.parseString(in)));
//        System.out.println("Part2: " + l.p2(l.parseString(in), offset));
    }

}
