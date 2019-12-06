package level_05;

import java.util.ArrayList;
import java.util.List;

abstract class Instruction {
    List<Integer> arguments = new ArrayList<>();

    abstract boolean executeOn(int[] data);

    abstract int argsLength();

    void addArg(int arg) {
        arguments.add(arg);
    }

    static Instruction parse(int[] data, int ip) {
        int instruction = data[ip];

        Instruction result;
        switch (Level05.getDigitAt(instruction, 0)) {
            case 1:
                result = new AddInstruction();
                break;
            case 2:
                result = new MultiplyInstruction();
                break;
            case 3:
                result = new InputInstruction();
                break;
            case 4:
                result = new OutputInstruction();
                break;
            case 99:
            default:
                result = new ExitInstruction();
                break;
        }

        for (int i = 1; i <= result.argsLength(); i++) {
            int iMode = Level05.getDigitAt(instruction, i + 2);
            int aVal = data[ip + i];
            if (i == result.argsLength() || iMode == 1) {
                result.addArg(aVal);
            } else if (iMode == 0) {
                result.addArg(data[aVal]);
            }
        }

        return result;
    }


}
