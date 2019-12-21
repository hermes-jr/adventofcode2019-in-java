package level_21;

import common.IntComp;
import common.Level;

import java.util.LinkedList;
import java.util.Queue;

public class Level21 extends Level {
    IntComp ic;
    String prog;
    private static final boolean VERBOSE = false;

    public Level21(String input) {
        prog = readResourcesFirstLine(input);
        ic = new IntComp(prog, 0);
    }

    public long p1() {
        /*
    #### -
    ###. -
    ##.# +
    ##.. -
    #.## -
    #.#. -
    #..# +
    #... -
    .### +
    .##. +-
    .#.# +
    .#.. +
    ..## +
    ..#. +-
    ...# +
    ABCD
    !A || (!C && D)
         */

        ic.run();
        printBotOutput();
        feedToBot("NOT A T");
        feedToBot("OR T J");
        feedToBot("NOT C T");
        feedToBot("AND D T");
        feedToBot("OR T J");
        feedToBot("WALK");
        long damage = ((LinkedList<Long>) ic.getOutput()).getLast();
        if (VERBOSE) printBotOutput();
        return damage;
    }

    private void printBotOutput() {
        System.out.println(readFromBot());
    }

    private String readFromBot() {
        Queue<Long> o = ic.getOutput();
        StringBuilder botOutput = new StringBuilder();
        while (!o.isEmpty()) {
            botOutput.append((char) o.poll().intValue());
        }
        return botOutput.toString();
    }

    private void feedToBot(String cmd) {
        for (char z : cmd.toCharArray()) {
            ic.addToInput(z);
        }
        ic.addToInput(10); // Newline in the end
        ic.run();
    }

    public static void main(String[] args) {
        Level21 l = new Level21("input");
        System.out.println("Part1: " + l.p1());
    }
}