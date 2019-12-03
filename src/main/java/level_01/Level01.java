package level_01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Level01 {
    public static void main(String[] args) throws IOException {
        List<Integer> data = new LinkedList<>();
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Level01.class.getClassLoader().getResourceAsStream("level_01/input"))))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(Integer.parseInt(line));
            }
        }
        int result = 0;
        for (int i : data) {
            result += step(i);
        }
        System.out.println("part1: " + result);

        result = 0;
        for (int i : data) {
            result += recursiveStep(i);
        }
        System.out.println("part2: " + result);
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
