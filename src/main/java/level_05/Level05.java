package level_05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

public class Level05 {

    int[] parseData(String indata) {
        String[] tokens = indata.split(",");
        int[] data = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Integer.parseInt(tokens[i]);
        }
        return data;
    }

    int p1(int[] data) {
        return runProg(data, 1);
    }

    int runProg(int[] ind, int input) {
        // protect in data
        int[] data = Arrays.copyOf(ind, ind.length);

        int ip = 0;

        while (true) {
            Instruction c = Instruction.parse(data, ip);
            if (c.executeOn(data)) {
                break;
            }
            ip += c.argsLength() + 1;
        }

        return data[0];
    }

    public String readResources(String filename) {
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_05/" + filename))))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }

    static int getDigitAt(int number, int pos) {
        int abs = (number < 0) ? -number : number;
        return pos == 0 ? abs % 100 : (abs / (int) (Math.pow(10, (pos - 1)))) % 10;
    }

    public static void main(String[] args) {
        Level05 l = new Level05();
        String in = l.readResources("input");
        int[] data = l.parseData(in);
        System.out.println("Part1: " + l.p1(data));
//        System.out.println("Part2: " + l.p2(data));
    }
}
