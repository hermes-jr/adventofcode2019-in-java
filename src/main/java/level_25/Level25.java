package level_25;

import common.IntComp;
import common.Level;

import java.util.Queue;
import java.util.Scanner;

public class Level25 extends Level {
    final IntComp ic;
    final String prog;
    private static final boolean VERBOSE = false;

    public Level25(String input) {
        prog = readResourcesFirstLine(input);
        ic = new IntComp(prog, 0);
    }

    /*
- semiconductor
- loom
- mutex
- asterisk
- wreath
- dark matter
- ornament (too heavy)
- sand
     */
    public void p1() {
        Scanner scanner = new Scanner(System.in);
        IntComp.ReturnReason returnReason = null;
        while (!IntComp.ReturnReason.HALTED.equals(returnReason)) {
            returnReason = ic.run();
            String s = readFromBot();
            /*System.out.println(s);
            String ui = scanner.nextLine();
            String cmd;
            switch (ui) {
                case "n":
                    cmd = "north";
                    break;
                case "s":
                    cmd = "south";
                    break;
                case "e":
                    cmd = "east";
                    break;
                case "w":
                    cmd = "west";
                    break;
                case "i":
                    cmd = "inv";
                    break;
                default:
                    cmd = ui;
            }
            feedToBot(cmd);*/
        }
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
        Level25 l = new Level25("input");
        l.p1();
    }
}