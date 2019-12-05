package level_05;

public interface Instruction {

    boolean executeOn(int[] data);

    int argsLength();

    static Instruction parse(int instruction) {
        switch (Level05.getDigitAt(instruction, 0)) {
            case 1:
                return new AddInstruction();
            case 2:
                return new MultiplyInstruction();
            case 99:
            default:
                return new ExitInstruction();
        }
    }


}
