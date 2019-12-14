package level_14;

import common.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level14 extends Level {
    Map<String, Reaction> knownReactions = new HashMap<>();
    Map<String, Integer> warehouse = new HashMap<>();

    Level14(String filename) {
        warehouse.put("ORE", Integer.MAX_VALUE); // :-)

        List<String> in = readResources(filename);
        for (String s : in) {
            // 5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV
            Map<String, Integer> requirements = new HashMap<>();
            String[] reaction = s.split(" => ");
            for (String r : reaction[0].split(", ")) {
                ImmutablePair<String, Integer> pr = Reaction.parseReaction(r);
                requirements.put(pr.getLeft(), pr.getRight());
            }
            ImmutablePair<String, Integer> pr = Reaction.parseReaction(reaction[1]);
            knownReactions.put(pr.getLeft(), new Reaction(pr.getLeft(), pr.getRight(), requirements));
        }
    }

    public static void main(String[] args) {
        Level14 l = new Level14("input");
        int orePerFuel = l.p1();
        System.out.println("Part1: " + orePerFuel);
    }

    int p1() {
        create("FUEL", 1);
        return Integer.MAX_VALUE - warehouse.get("ORE");
    }

    private void create(String chemical, int required) {
        int inStock = warehouse.getOrDefault(chemical, 0);
        while (inStock < required) {
            Reaction reaction = knownReactions.get(chemical);
            for (Map.Entry<String, Integer> req : reaction.getRequirements().entrySet()) {
                create(req.getKey(), req.getValue());
                warehouse.put(req.getKey(), warehouse.getOrDefault(req.getKey(), 0) - req.getValue());
            }
            // Add reaction result to warehouse
            inStock += reaction.quantity;
            warehouse.put(chemical, inStock);
        }
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static class Reaction {
        @EqualsAndHashCode.Include
        String chemical;
        Integer quantity;
        Map<String, Integer> requirements;

        static ImmutablePair<String, Integer> parseReaction(String s) {
            String[] e = s.split(" ");
            return ImmutablePair.of(e[1].trim(), Integer.parseInt(e[0].trim()));
        }
    }

}
