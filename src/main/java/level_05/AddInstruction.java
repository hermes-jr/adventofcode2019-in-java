package level_05;

public class AddInstruction extends Instruction {
    @Override
    public boolean executeOn(int[] data) {
        data[arguments.get(2)] = arguments.get(0) +  arguments.get(1);
        return false;
    }

    @Override
    int argsLength() {
        return 3;
    }
}
