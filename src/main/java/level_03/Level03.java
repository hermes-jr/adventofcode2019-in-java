package level_03;

import level_01.Level01;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Level03 {
    public static void main(String[] args) {
        Level03 l = new Level03();
        int result = l.processResource("input");
        System.out.println("Result1: " + result);
    }

    public int processResource(String fname) {
        Set<ImmutablePair<Integer, Integer>> wire1 = new LinkedHashSet<>();
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Level01.class.getClassLoader().getResourceAsStream("level_03/" + fname))))) {
            wire1 = parseLine(br.readLine());
            Set<ImmutablePair<Integer, Integer>> wire2 = parseLine(br.readLine());
            wire1.retainAll(wire2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int d = Integer.MAX_VALUE;
        for (ImmutablePair<Integer, Integer> ip : wire1) {
            if (ip.equals(new ImmutablePair<>(0, 0))) {
                continue;
            }
            int nd = Math.abs(ip.getLeft()) + Math.abs(ip.getRight());
            if (nd < d) {
                d = nd;
            }
        }
        return d;
    }

    Set<ImmutablePair<Integer, Integer>> parseLine(String readLine) {
        Set<ImmutablePair<Integer, Integer>> result = new HashSet<>();
        result.add(new ImmutablePair<>(0, 0));
        int x = 0;
        int y = 0;
        for (String cmd : readLine.split(",")) {
            char direction = cmd.charAt(0);
            int distance = Integer.parseInt(cmd.substring(1));
            int dx = 0, dy = 0;
            switch (direction) {
                case 'L':
                    dx = -1;
                    break;
                case 'R':
                    dx = 1;
                    break;
                case 'U':
                    dy = 1;
                    break;
                case 'D':
                    dy = -1;
                    break;
            }
            for (int i = 0; i < distance; i++) {
                x += dx;
                y += dy;
                result.add(new ImmutablePair<>(x, y));
            }
        }
        return result;
    }


}
