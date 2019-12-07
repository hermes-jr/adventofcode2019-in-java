package level_07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Level07 {
    private int[] data;

    int[] parseData(String indata) {
        String[] tokens = indata.split(",");
        int[] result = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            result[i] = Integer.parseInt(tokens[i]);
        }
        data = result;
        return result;
    }

    int p1() {
        int maxResult = Integer.MIN_VALUE;

        for (List<Integer> perm : generatePerm(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)))) {
            int res = calcForPhase(new LinkedList<>(perm));
            if (res >= maxResult) {
                maxResult = res;
            }
        }
        return maxResult;
    }

    int calcForPhase(List<Integer> phase) {
        int input = 0;
        for (Integer iv : phase) {
            Deque<Integer> stack = new LinkedList<>();
            stack.add(iv);
            stack.add(input);
            input = runProg(stack).poll();
        }
        return input;
    }

    Queue<Integer> runProg(Queue<Integer> input) {
        Queue<Integer> programLog = new LinkedList<>();
        int[] rd = Arrays.copyOf(data, data.length);

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
                    data[args[0]] = input.poll();
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

        data = rd;
        return programLog;
    }

    public int[] parseArgs(int ip, int argsLength) {
        return parseArgs(ip, argsLength, false);
    }

    public int[] parseArgs(int ip, int argsLength, boolean jmp) {
        int[] result = new int[argsLength];
        for (int i = 0; i < argsLength; i++) {
            int iMode = Level07.getDigitAt(data[ip], i + 3);
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

    public String readResources(String filename) {
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_07/" + filename))))) {
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

    public <E> List<List<E>> generatePerm(List<E> original) {
        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<E>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<E>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    public static void main(String[] args) {
        Level07 l = new Level07();
        String in = l.readResources("input");
        l.data = l.parseData(in);
        System.out.println("Part1: " + l.p1());
    }
}
