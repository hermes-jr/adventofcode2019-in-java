package level_05;

public class ExitInstruction extends Instruction {
    @Override
    boolean executeOn(int[] data) {
        return true;
    }

    @Override
    int argsLength() {
        return 0;
    }
}
