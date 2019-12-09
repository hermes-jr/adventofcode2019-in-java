package level_09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
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

    Map<BigInteger, BigInteger> parseData(String indata) {
        String[] tokens = indata.split(",");
        Map<BigInteger, BigInteger> result = new HashMap<>();
        for (int i = 0; i < tokens.length; i++) {
            result.put(BigInteger.valueOf(i), BigInteger.valueOf(Long.parseLong(tokens[i])));
        }
        return result;
    }

    BigInteger p1(String s) {
        IntComp ic = new IntComp(parseData(s), 0);
        ic.input.add(BigInteger.ONE);
        ic.run();
        return ic.output.peek();
    }

    BigInteger p2(String s) {
        IntComp ic = new IntComp(parseData(s), 1);
        ic.input.add(BigInteger.valueOf(2));
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

    static int getDigitAt(BigInteger number, int pos) {
        BigInteger abs = number.abs();
        return pos == 0 ? abs.mod(BigInteger.valueOf(100L)).intValue() : abs.divide(BigInteger.TEN.pow(pos - 1)).mod(BigInteger.TEN).intValue();

    }

    public static void main(String[] args) {
        Level09 l = new Level09();
        String in = l.readResources("input");
        System.out.println("Part1: " + l.p1(in));
        System.out.println("Part1: " + l.p2(in));
    }

    static class IntComp {
        Map<BigInteger, BigInteger> data;
        int id;
        BigInteger ip = BigInteger.ZERO;
        BigInteger relBase = BigInteger.ZERO;
        Queue<BigInteger> input = new LinkedList<>();
        Queue<BigInteger> output = new LinkedList<>();

        IntComp(Map<BigInteger, BigInteger> initialState, int id) {
            this.id = id;
            this.data = initialState;
        }

        ReturnReason run() {
            while (true) {
                int iCode = data.getOrDefault(ip, BigInteger.ZERO).mod(BigInteger.valueOf(100L)).intValue();
                int[] argModes = getInstructionModes(data.getOrDefault(ip, BigInteger.ZERO).divide(BigInteger.valueOf(100)), argSizes.get(iCode));
                BigInteger v1, v2;
                switch (iCode) {
                    case 1: // add
                        v1 = readValue(ip, 0, argModes);
                        v2 = readValue(ip, 1, argModes);
                        writeValue(ip, 2, argModes, v1.add(v2));
                        ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        break;
                    case 2: // mul
                        v1 = readValue(ip, 0, argModes);
                        v2 = readValue(ip, 1, argModes);
                        writeValue(ip, 2, argModes, v1.multiply(v2));
                        ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        break;
                    case 3: // in
                        if (input.peek() == null) {
                            return ReturnReason.NO_INPUT;
                        }
                        writeValue(ip, 0, argModes, input.poll());
                        ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        break;
                    case 4: // out
                        v1 = readValue(ip, 0, argModes);
                        output.add(v1);
                        ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        break;
                    case 5: // jit
                        v1 = readValue(ip, 0, argModes);
                        v2 = readValue(ip, 1, argModes);
                        if (v1.signum() != 0) {
                            ip = v2;
                        } else {
                            ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        }
                        break;
                    case 6: // jif
                        v1 = readValue(ip, 0, argModes);
                        v2 = readValue(ip, 1, argModes);
                        if (v1.signum() == 0) {
                            ip = v2;
                        } else {
                            ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        }
                        break;
                    case 7: // lt
                        v1 = readValue(ip, 0, argModes);
                        v2 = readValue(ip, 1, argModes);
                        writeValue(ip, 2, argModes, (v1.compareTo(v2) < 0) ? BigInteger.ONE : BigInteger.ZERO);
                        ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        break;
                    case 8: // eq
                        v1 = readValue(ip, 0, argModes);
                        v2 = readValue(ip, 1, argModes);
                        writeValue(ip, 2, argModes, (v1.compareTo(v2) == 0) ? BigInteger.ONE : BigInteger.ZERO);
                        ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        break;
                    case 9: // relbase
                        v1 = readValue(ip, 0, argModes);
                        relBase = relBase.add(v1);
                        ip = ip.add(BigInteger.valueOf(argSizes.get(iCode) + 1));
                        break;
                    case 99:
                        return ReturnReason.HALTED;
                    default:
                        throw new RuntimeException("Unknown instruction");
                }
            }
        }

        BigInteger readValue(BigInteger ip, int argNum, int[] argModes) {
            BigInteger argVal = data.getOrDefault(ip.add(BigInteger.valueOf(argNum + 1)), BigInteger.ZERO);
            if (argModes[argNum] == 0) {
                return data.getOrDefault(argVal, BigInteger.ZERO);
            } else if (argModes[argNum] == 1) {
                return argVal;
            } else if (argModes[argNum] == 2) {
                return data.getOrDefault(argVal.add(relBase), BigInteger.ZERO);
            }
            throw new RuntimeException("Unknown argument mode");
        }

        void writeValue(BigInteger ip, int argNum, int[] argModes, BigInteger value) {
            BigInteger argVal = data.getOrDefault(ip.add(BigInteger.valueOf(argNum + 1)), BigInteger.ZERO);
            if (argModes[argNum] == 0) {
                data.put(argVal, value);
            } else if (argModes[argNum] == 2) {
                data.put(argVal.add(relBase), value);
            }
        }

        public int[] getInstructionModes(BigInteger instuction, int argsLength) {
            int[] result = new int[argsLength];
            for (int i = 0; i < argsLength; i++) {
                result[i] = instuction.mod(BigInteger.TEN).intValue();
                instuction = instuction.divide(BigInteger.TEN);
            }
            return result;
        }

    }
}
