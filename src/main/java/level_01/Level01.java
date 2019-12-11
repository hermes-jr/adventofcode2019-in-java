package level_01;

import common.Level;

import java.util.List;
import java.util.stream.Collectors;

public class Level01 extends Level {
    public static void main(String[] args) {
        List<Integer> data = readResources(Level01.class, "input").stream().map(Integer::parseInt).collect(Collectors.toList());
        int result = 0;
        for (int i : data) {
            result += step(i);
        }
        System.out.println("Part1: " + result);

        result = 0;
        for (int i : data) {
            result += recursiveStep(i);
        }
        System.out.println("Part2: " + result);
    }

    private static int recursiveStep(int a) {
        int v = step(a);
        if (v == 0) return 0;
        return v + recursiveStep(v);
    }

    private static int step(int a) {
        int result = a / 3 - 2;
        return Math.max(result, 0);
    }
}
