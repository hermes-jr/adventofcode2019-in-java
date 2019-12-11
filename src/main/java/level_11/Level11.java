package level_11;

import common.IntComp;
import common.Level;

public class Level11 extends Level {
    IntComp ic;

    public Level11(String input) {
        String prog = readResourcesFirstLine(Level11.class, input);
        ic = new IntComp(prog, 0);
    }

    public int p1() {
        paint();
        return -1;
    }

    private void paint() {
        IntComp.ReturnReason lastReason = null;
        while (lastReason != IntComp.ReturnReason.HALTED) {
            ic.addToInput(0L);
            lastReason = ic.run();
            long color = ic.getOutput().poll();
            long direction = ic.getOutput().poll();
            System.out.println(color + " " + direction);
        }
    }

    public static void main(String[] args) {
        Level11 l = new Level11("input");
        System.out.println("Part1: " + l.p1());
    }

}