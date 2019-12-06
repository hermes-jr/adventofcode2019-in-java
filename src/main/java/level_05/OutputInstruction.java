package level_05;

public class OutputInstruction extends Instruction {
    @Override
    boolean executeOn(int[] data) {
        System.out.println("PROGOUT: " + data[arguments.get(0)]);
        return false;
    }

    @Override
    int argsLength() {
        return 1;
    }
}
