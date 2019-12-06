package level_05;

public class InputInstruction extends Instruction {
    @Override
    boolean executeOn(int[] data) {
        data[arguments.get(0)] = 1;
        return false;
    }

    @Override
    int argsLength() {
        return 1;
    }
}
