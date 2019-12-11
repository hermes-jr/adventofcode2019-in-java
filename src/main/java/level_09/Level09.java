package level_09;

import common.IntComp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Level09 {

    Long p1(String s) {
        IntComp ic = new IntComp(s, 0);
        ic.addToInput(1L);
        ic.run();
        return ic.getOutput().peek();
    }

    Long p2(String s) {
        IntComp ic = new IntComp(s, 1);
        ic.addToInput(2L);
        ic.run();
        return ic.getOutput().peek();
    }

    public String readResources(String filename) {
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_" + getClass().getSimpleName().substring(5) + "/" + filename))))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }

    public static void main(String[] args) {
        Level09 l = new Level09();
        String in = l.readResources("input");
        System.out.println("Part1: " + l.p1(in));
        System.out.println("Part1: " + l.p2(in));
    }

}
