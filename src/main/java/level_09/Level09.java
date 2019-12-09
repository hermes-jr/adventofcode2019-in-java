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
        System.out.println(ic.output);
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
//        System.out.println("Part2: " + l.p2());
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
                int icode = getDigitAt(data.getOrDefault(ip, BigInteger.ZERO), 0);
                BigInteger[] args;
                switch (icode) {
                    case 1: // add
                        args = parseArgs(ip, 3);
                        data.put(args[2], args[0].add(args[1]));
                        ip = ip.add(BigInteger.valueOf(4));
                        break;
                    case 2: // mul
                        args = parseArgs(ip, 3);
                        data.put(args[2], args[0].multiply(args[1]));
                        ip = ip.add(BigInteger.valueOf(4));
                        break;
                    case 3: // in
                        args = parseArgs(ip, 1);
                        if (input.peek() == null) {
                            return ReturnReason.NO_INPUT;
                        }
                        data.put(args[0], input.poll());
                        ip = ip.add(BigInteger.valueOf(2));
                        break;
                    case 4: // out
                        args = parseArgs(ip, 1, true);
                        output.add(args[0]);
                        ip = ip.add(BigInteger.valueOf(2));
                        break;
                    case 5: // jit
                        args = parseArgs(ip, 2, true);
                        if (args[0].signum() != 0) {
                            ip = args[1];
                        } else {
                            ip = ip.add(BigInteger.valueOf(3));
                        }
                        break;
                    case 6: // jif
                        args = parseArgs(ip, 2, true);
                        if (args[0].signum() == 0) {
                            ip = args[1];
                        } else {
                            ip = ip.add(BigInteger.valueOf(3));
                        }
                        break;
                    case 7: // lt
                        args = parseArgs(ip, 3);
                        data.put(args[2], (args[0].compareTo(args[1]) < 0) ? BigInteger.ONE : BigInteger.ZERO);
                        ip = ip.add(BigInteger.valueOf(4));
                        break;
                    case 8: // eq
                        args = parseArgs(ip, 3);
                        data.put(args[2], (args[0].equals(args[1])) ? BigInteger.ONE : BigInteger.ZERO);
                        ip = ip.add(BigInteger.valueOf(4));
                        break;
                    case 9: // relbase
                        args = parseArgs(ip, 1);
                        relBase = relBase.add(args[0]);
                        ip = ip.add(BigInteger.valueOf(2));
                        break;
                    case 99:
                        return ReturnReason.HALTED;
                    default:
                        throw new RuntimeException("Unknown instruction");
                }
            }
        }

        public BigInteger[] parseArgs(BigInteger ip, int argsLength) {
            return parseArgs(ip, argsLength, false);
        }

        public BigInteger[] parseArgs(BigInteger ip, int argsLength, boolean jmp) {
            BigInteger[] result = new BigInteger[argsLength];
            for (int i = 0; i < argsLength; i++) {
                int iMode = Level09.getDigitAt(data.getOrDefault(ip, BigInteger.ZERO), i + 3);
                BigInteger aVal = data.getOrDefault(ip.add(BigInteger.valueOf(i)).add(BigInteger.ONE), BigInteger.ZERO);
                if (iMode == 2) {
                    result[i] = data.getOrDefault(aVal.add(relBase), BigInteger.ZERO);
                } else if (!jmp && i + 1 == argsLength) {
                    result[i] = aVal;
                } else if (iMode == 1) {
                    result[i] = aVal;
                } else if (iMode == 0) {
                    result[i] = data.getOrDefault(aVal, BigInteger.ZERO);
                }
            }
            return result;
        }

    }
}
