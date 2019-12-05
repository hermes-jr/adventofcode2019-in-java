package level_05;

public class ExitInstruction implements Instruction {
    @Override
    public boolean executeOn(int[] data) {
        return true;
    }

    @Override
    public int argsLength() {
        return 0;
    }
}
