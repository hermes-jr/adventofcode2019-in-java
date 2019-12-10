package level_09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Level09 {
    enum ReturnReason {
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

    Map<Long, Long> parseData(String indata) {
        String[] tokens = indata.split(",");
        Map<Long, Long> result = new HashMap<>();
        for (int i = 0; i < tokens.length; i++) {
            result.put((long) i, Long.parseLong(tokens[i]));
        }
        return result;
    }

    Long p1(String s) {
        IntComp ic = new IntComp(parseData(s), 0);
        ic.input.add(1L);
        ic.run();
        return ic.output.peek();
    }

    Long p2(String s) {
        IntComp ic = new IntComp(parseData(s), 1);
        ic.input.add(2L);
        ic.run();
        return ic.output.peek();
    }

    public String readResources(String filename) {
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_" + getClass().getSimpleName().substring(5) + "/" + filename))))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Level09 l = new Level09();
        String in = l.readResources("input");
        System.out.println("Part1: " + l.p1(in));
        System.out.println("Part1: " + l.p2(in));
        long time = System.currentTimeMillis() - start;
        System.out.println("Time: " + time);
    }

    static class IntComp {
        Map<Long, Long> data;
        int id;
        long ip;
        long relBase;
        Queue<Long> input = new LinkedList<>();
        Queue<Long> output = new LinkedList<>();

        IntComp(Map<Long, Long> initialState, int id) {
            this.id = id;
            this.data = initialState;
        }

        ReturnReason run() {
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
                    case 9: // relbase
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

        public int[] getInstructionModes(Long instruction, int argsLength) {
            int[] result = new int[argsLength];
            for (int i = 0; i < argsLength; i++) {
                result[i] = Math.toIntExact(instruction % 10L);
                instruction /= 10L;
            }
            return result;
        }

    }
}
