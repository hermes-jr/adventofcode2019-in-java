package level_25;

import common.IntComp;
import common.Level;
import org.apache.commons.math3.util.Combinations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Level25 extends Level {
    final IntComp ic;
    private static final boolean VERBOSE = false;

    public Level25(String input) {
        ic = new IntComp(readResourcesFirstLine(input), 0);
    }

    // I've manually mapped the place, too lazy to automate. Only the combination search will be coded
    public String p1() {
        List<String> pickUpEverything = Arrays.asList(
                "east",
                // "east", "take ornament", "west", // Too heavy alone, can be skipped
                "take semiconductor",
                "west", "north", "east", "north",
                "take loom",
                "south", "west", "north", "north",
                "take mutex",
                "south", "south", "south",
                "west", "west", "south", "east",
                "take asterisk",
                "north",
                "take wreath",
                "south", "west", "north",
                "take sand",
                "north",
                "take dark matter",
                "east"
        );
        for (String c : pickUpEverything) {
            ic.run();
            feedToBot(c);
            if (VERBOSE) {
                printBotOutput();
            } else {
                readFromBot();
            }
        }

        // Drop everything on the floor
        List<String> items = Arrays.asList("semiconductor", "loom", "mutex", "sand", "asterisk", "wreath", "dark matter");
        for (String i : items) {
            feedToBot("drop " + i);
            ic.run();
            readFromBot(); // Discard
        }

        List<List<String>> allOptions = getAllPossibleCombinations(items);
        if (VERBOSE) System.out.println(allOptions);

        // Pick up only the needed items, try them, drop everything, repeat
        for (List<String> option : allOptions) {
            for (String item : option) {
                feedToBot("take " + item);
                ic.run();
                readFromBot();
            }
            feedToBot("east"); // try
            IntComp.ReturnReason returnReason = ic.run();
            String s = readFromBot();
            if (VERBOSE) System.out.println(s);
            if (IntComp.ReturnReason.HALTED.equals(returnReason)) {
                return parseFinalMessage(s);
            }
            for (String item : option) {
                feedToBot("drop " + item);
                ic.run();
                readFromBot();
            }
        }
        return null;
    }

    private String parseFinalMessage(String s) {
        Pattern finalMessagePattern = Pattern.compile(".*You should be able to get in by typing ([0-9]+).*", Pattern.DOTALL);
        Matcher m = finalMessagePattern.matcher(s);
        if (m.matches()) {
            return m.group(1);
        }
        return s;
    }

    private List<List<String>> getAllPossibleCombinations(List<String> items) {
        List<List<String>> allOptions = new ArrayList<>();
        for (int i = 1; i <= items.size(); i++) {
            for (int[] comb : new Combinations(items.size(), i)) {
                List<String> r = new ArrayList<>();
                for (int idx : comb) {
                    r.add(items.get(idx));
                }
                allOptions.add(r);
            }
        }
        return allOptions;
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
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: Happy Holidays!");
    }
}