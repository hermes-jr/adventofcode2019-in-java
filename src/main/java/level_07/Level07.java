package level_07;

import common.Level;

import java.util.*;

public class Level07 extends Level {
    enum ReturnReason {
        HALTED,
        NO_INPUT
    }

    static int[] initialState;

    int[] parseData(String indata) {
        String[] tokens = indata.split(",");
        int[] result = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            result[i] = Integer.parseInt(tokens[i]);
        }
        initialState = result;
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

    int p2() {
        int maxResult = Integer.MIN_VALUE;

        for (List<Integer> perm : generatePerm(new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9)))) {
            int res = calcForPhaseCyclic(new LinkedList<>(perm));
            if (res >= maxResult) {
                maxResult = res;
            }
        }
        return maxResult;
    }

    int calcForPhase(List<Integer> phase) {
        Integer nextInput = 0;
        Amp[] amps = new Amp[phase.size()];
        for (int i = 0; i < phase.size(); i++) {
            amps[i] = new Amp(initialState, i);
        }
        for (int i = 0; i < phase.size(); i++) {
            Queue<Integer> stack = new LinkedList<>();
            stack.add(phase.get(i));
            stack.add(nextInput);
            amps[i].input = stack;
            ReturnReason ampResult = amps[i].run();
            if (ampResult.equals(ReturnReason.HALTED) && amps[i].output.peek() != null) {
                nextInput = amps[i].output.poll();
            }
        }
        if (nextInput == null) {
            throw new RuntimeException("Unexpected result of program execution");
        }
        return nextInput;
    }

    int calcForPhaseCyclic(List<Integer> phase) {
        Queue<Integer> nextInput = new LinkedList<>();
        Amp[] amps = new Amp[phase.size()];
        for (int i = 0; i < phase.size(); i++) {
            amps[i] = new Amp(initialState, i);
            amps[i].input.add(phase.get(i));
            if (i == 0) {
                amps[i].input.add(0);
            }
        }
        int iter = 0;

        while (true) {
            Amp currentAmp = amps[iter % (phase.size())];
            currentAmp.input.addAll(nextInput);
            nextInput.clear();
            ReturnReason ampResult = currentAmp.run();

            if (currentAmp.id + 1 == phase.size() && ampResult.equals(ReturnReason.HALTED)) {
                if (currentAmp.output.peek() == null) {
                    throw new RuntimeException("Halted on empty result. Something is wrong");
                }
                return currentAmp.output.poll();
            }
            nextInput.addAll(currentAmp.output);
            currentAmp.output.clear();
            iter++;
        }
    }

    static int getDigitAt(int number, int pos) {
        int abs = (number < 0) ? -number : number;
        return pos == 0 ? abs % 100 : (abs / (int) (Math.pow(10, (pos - 1)))) % 10;
    }

    public <E> List<List<E>> generatePerm(List<E> original) {
        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    public static void main(String[] args) {
        Level07 l = new Level07();
        String in = readResourcesFirstLine(Level07.class, "input");
        initialState = l.parseData(in);
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2());
    }

    static class Amp {
        int[] data;
        int id;
        int ip = 0;
        Queue<Integer> input = new LinkedList<>();
        Queue<Integer> output = new LinkedList<>();

        Amp(int[] initialState, int id) {
            this.id = id;
            this.data = Arrays.copyOf(initialState, initialState.length);
        }

        ReturnReason run() {
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
                        if (input.peek() == null) {
                            return ReturnReason.NO_INPUT;
                        }
                        data[args[0]] = input.poll();
                        ip += 2;
                        break;
                    case 4: // out
                        args = parseArgs(ip, 1, true);
                        output.add(args[0]);
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
                        return ReturnReason.HALTED;
                    default:
                        throw new RuntimeException("Unknown instruction");
                }
            }
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

    }
}
