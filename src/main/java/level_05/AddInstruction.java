package level_05;

public class AddInstruction implements Instruction {
    @Override
    public boolean executeOn(int[] data) {
        return false;
    }

    @Override
    public int argsLength() {
        return 3;
    }
}
