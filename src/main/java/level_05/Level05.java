package level_05;

import common.IntComp;
import common.Level;

import java.util.LinkedList;

public class Level05 extends Level {
    IntComp ic;

    Long p1() {
        ic.addToInput(1L);
        ic.run();
        return ((LinkedList<Long>) ic.getOutput()).get(ic.getOutput().size() - 1);
    }

    Long p2() {
        ic.addToInput(5L);
        ic.run();
        return ic.getOutput().poll();
    }

    public static void main(String[] args) {
        Level05 l = new Level05();
        String in = readResourcesFirstLine(Level05.class, "input");
        l.ic = new IntComp(in, 0);
        System.out.println("Part1: " + l.p1());
        l.ic = new IntComp(in, 1);
        System.out.println("Part2: " + l.p2());
    }
}
