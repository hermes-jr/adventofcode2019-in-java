package level_22;

import common.Level;

import java.util.List;

public class Level22 extends Level {
    public static void main(String[] args) {
        Level22 l = new Level22();
        List<String> commands = l.readResources("input");
        for(String s: commands) {
            System.out.println(s);
        }
    }
}
