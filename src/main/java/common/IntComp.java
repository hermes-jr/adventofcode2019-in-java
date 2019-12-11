package common;

import java.util.*;

public class IntComp {
    Map<Long, Long> data;
    String originalProg;
    int id;
    long ip;
    long relBase;
    Queue<Long> input = new LinkedList<>();
    Queue<Long> output = new LinkedList<>();

    public IntComp(String s, int id) {
        originalProg = s;
        this.id = id;
        this.data = parseProg(originalProg);
    }

    private Map<Long, Long> parseProg(String s) {
        String[] tokens = s.split(",");
        Map<Long, Long> parsedProg = new HashMap<>();
        for (int i = 0; i < tokens.length; i++) {
            parsedProg.put((long) i, Long.parseLong(tokens[i]));
        }
        return parsedProg;
    }

    public void addToInput(long p) {
        input.add(p);
    }

    public Queue<Long> getOutput() {
        return output;
    }

    public long getRelBase() {
        return this.relBase;
    }

    public void setRelBase(long l) {
        this.relBase = l;
    }

    public Map<Long, Long> getData() {
        return data;
    }

    public void reset() {
        this.data = parseProg(originalProg);
        this.output.clear();
        this.input.clear();
        this.ip = 0L;
        this.relBase = 0L;
    }

    public enum ReturnReason {
        HALTED,
        NO_INPUT
    }

    static Map<Integer, Integer> argSizes = new HashMap<>();

    static {
        argSizes.put(1, 3);
        argSizes.put(2, 3);
        argSizes.put(3, 1);
        argSizes.put(4, 1);
        argSizes.put(5, 2);
        argSizes.put(6, 2);
        argSizes.put(7, 3);
        argSizes.put(8, 3);
        argSizes.put(9, 1);
        argSizes.put(99, 0);
    }

    public ReturnReason run(Collection<Long> inData) {
        this.input.addAll(inData);
        return run();
    }

    public ReturnReason run(long singleArg) {
        this.input.add(singleArg);
        return run();
    }

    public ReturnReason run() {
        while (true) {
            int iCode = Math.toIntExact(data.getOrDefault(ip, 0L) % 100L);
            int[] argModes = getInstructionModes(data.getOrDefault(ip, 0L) / 100L, argSizes.get(iCode));
            long v1, v2;
            switch (iCode) {
                case 1: // add
                    v1 = readValue(ip, 0, argModes);
                    v2 = readValue(ip, 1, argModes);
                    writeValue(ip, 2, argModes, v1 + v2);
                    ip += argSizes.get(iCode) + 1L;
                    break;
                case 2: // mul
                    v1 = readValue(ip, 0, argModes);
                    v2 = readValue(ip, 1, argModes);
                    writeValue(ip, 2, argModes, v1 * v2);
                    ip += argSizes.get(iCode) + 1L;
                    break;
                case 3: // in
                    if (input.peek() == null) {
                        return ReturnReason.NO_INPUT;
                    }
                    writeValue(ip, 0, argModes, input.poll());
                    ip += argSizes.get(iCode) + 1L;
                    break;
                case 4: // out
                    v1 = readValue(ip, 0, argModes);
                    output.add(v1);
                    ip += argSizes.get(iCode) + 1L;
                    break;
                case 5: // jit
                    v1 = readValue(ip, 0, argModes);
                    v2 = readValue(ip, 1, argModes);
                    if (v1 != 0L) {
                        ip = v2;
                    } else {
                        ip += argSizes.get(iCode) + 1L;
                    }
                    break;
                case 6: // jif
                    v1 = readValue(ip, 0, argModes);
                    v2 = readValue(ip, 1, argModes);
                    if (v1 == 0L) {
                        ip = v2;
                    } else {
                        ip += argSizes.get(iCode) + 1L;
                    }
                    break;
                case 7: // lt
                    v1 = readValue(ip, 0, argModes);
                    v2 = readValue(ip, 1, argModes);
                    writeValue(ip, 2, argModes, (v1 < v2) ? 1L : 0L);
                    ip += argSizes.get(iCode) + 1L;
                    break;
                case 8: // eq
                    v1 = readValue(ip, 0, argModes);
                    v2 = readValue(ip, 1, argModes);
                    writeValue(ip, 2, argModes, (v1 == v2) ? 1L : 0L);
                    ip += argSizes.get(iCode) + 1L;
                    break;
                case 9: // relative base
                    v1 = readValue(ip, 0, argModes);
                    relBase += v1;
                    ip += argSizes.get(iCode) + 1L;
                    break;
                case 99:
                    return ReturnReason.HALTED;
                default:
                    throw new RuntimeException("Unknown instruction");
            }
        }
    }

    Long readValue(long ip, int argNum, int[] argModes) {
        Long argVal = data.getOrDefault(ip + argNum + 1L, 0L);
        if (argModes[argNum] == 0) {
            return data.getOrDefault(argVal, 0L);
        } else if (argModes[argNum] == 1) {
            return argVal;
        } else if (argModes[argNum] == 2) {
            return data.getOrDefault(argVal + relBase, 0L);
        }
        throw new RuntimeException("Unknown argument mode");
    }

    void writeValue(long ip, int argNum, int[] argModes, long value) {
        Long argVal = data.getOrDefault(ip + argNum + 1L, 0L);
        if (argModes[argNum] == 0) {
            data.put(argVal, value);
        } else if (argModes[argNum] == 2) {
            data.put(argVal + relBase, value);
        }
    }

    int[] getInstructionModes(Long instruction, int argsLength) {
        int[] result = new int[argsLength];
        for (int i = 0; i < argsLength; i++) {
            result[i] = Math.toIntExact(instruction % 10L);
            instruction /= 10L;
        }
        return result;
    }

}