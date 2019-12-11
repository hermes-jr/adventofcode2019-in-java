package level_05;

import common.Level;

import java.util.LinkedList;
import java.util.List;

public class Level05 extends Level {
    private int[] data;

    int[] parseData(String in) {
        String[] tokens = in.split(",");
        int[] result = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            result[i] = Integer.parseInt(tokens[i]);
        }
        data = result;
        return result;
    }

    int p1() {
        List<Integer> result = runProg(1);
        return result.get(result.size() - 1);
    }

    int p2() {
        List<Integer> result = runProg(5);
        return result.get(result.size() - 1);
    }

    // Wrapper method for testing
    List<Integer> runProg(int[] d, int input) {
        data = d;
        return runProg(input);
    }

    List<Integer> runProg(int input) {
        List<Integer> programLog = new LinkedList<>();

        int ip = 0;

        mainLoop:
        while (true) {
            int icode = getDigitAt(data[ip], 0);
            int[] args;
            switch (icode) {
                case 1: // add
                    args = parseArgs(ip, 3);
                    data[args[2]] = args[0] + args[1];
                    ip += 4;
                    break;
                case 2: // mul
                    args = parseArgs(ip, 3);
                    data[args[2]] = args[0] * args[1];
                    ip += 4;
                    break;
                case 3: // in
                    args = parseArgs(ip, 1);
                    data[args[0]] = input;
                    ip += 2;
                    break;
                case 4: // out
                    args = parseArgs(ip, 1, true);
                    programLog.add(args[0]);
                    ip += 2;
                    break;
                case 5: // jit
                    args = parseArgs(ip, 2, true);
                    if (args[0] != 0) ip = args[1];
                    else ip += 3;
                    break;
                case 6: // jif
                    args = parseArgs(ip, 2, true);
                    if (args[0] == 0) ip = args[1];
                    else ip += 3;
                    break;
                case 7: // lt
                    args = parseArgs(ip, 3);
                    data[args[2]] = (args[0] < args[1]) ? 1 : 0;
                    ip += 4;
                    break;
                case 8: // eq
                    args = parseArgs(ip, 3);
                    data[args[2]] = (args[0] == args[1]) ? 1 : 0;
                    ip += 4;
                    break;
                case 99:
                default:
                    break mainLoop;
            }
        }

        return programLog;
    }

    public int[] parseArgs(int ip, int argsLength) {
        return parseArgs(ip, argsLength, false);
    }

    public int[] parseArgs(int ip, int argsLength, boolean jmp) {
        int[] result = new int[argsLength];
        for (int i = 0; i < argsLength; i++) {
            int iMode = Level05.getDigitAt(data[ip], i + 3);
            int aVal = data[ip + i + 1];
            if (!jmp && i + 1 == argsLength) {
                result[i] = aVal;
            } else if (iMode == 1) {
                result[i] = aVal;
            } else if (iMode == 0) {
                result[i] = data[aVal];
            }
        }
        return result;
    }

    static int getDigitAt(int number, int pos) {
        int abs = (number < 0) ? -number : number;
        return pos == 0 ? abs % 100 : (abs / (int) (Math.pow(10, (pos - 1)))) % 10;
    }

    public static void main(String[] args) {
        Level05 l = new Level05();
        String in = readResourcesFirstLine(Level05.class, "input");
        l.data = l.parseData(in);
        System.out.println("Part1: " + l.p1());
        l.data = l.parseData(in);
        System.out.println("Part2: " + l.p2());
    }

    public int[] getData() {
        return data;
    }
}
