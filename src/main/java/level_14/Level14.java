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
    Map<String, Long> warehouse = new HashMap<>();
    final long ORE_LIMIT = 1000000000000L;

    Level14(String filename) {
        List<String> in = readResources(filename);
        for (String s : in) {
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

    long p1() {
        return create("FUEL", 1L);
    }

    long p2(long orePerFuel) {
        long low = ORE_LIMIT / orePerFuel;
        return binarySearch(low, low * 2);
    }

    public static void main(String[] args) {
        Level14 l = new Level14("input");
        long orePerFuel = l.p1();
        System.out.println("Part1: " + orePerFuel);
        System.out.println("Part2: " + l.p2(orePerFuel));
    }

    private long binarySearch(long low, long high) {
        if (high >= low) {
            long mid = low + (high - low) / 2;
            long oreRequired = create("FUEL", mid);
            if (oreRequired == ORE_LIMIT)
                return mid;
            if (oreRequired > ORE_LIMIT)
                return binarySearch(low, mid - 1);
            return binarySearch(mid + 1, high);
        }
        return low - 1;
    }

    private long create(String chemical, long required) {
        if ("FUEL".equals(chemical)) {
            warehouse.clear();
        }
        if ("ORE".equals(chemical)) {
            return required;
        }
        Reaction reaction = knownReactions.get(chemical);
        long toCreate = required - warehouse.getOrDefault(chemical, 0L);
        if (toCreate < 1) {
            return 0L; // served from warehouse
        }
        long batchSize = (long) Math.ceil(toCreate / (double) reaction.getQuantity());
        for (Map.Entry<String, Integer> req : reaction.getRequirements().entrySet()) {
            create(req.getKey(), req.getValue() * batchSize);
            warehouse.put(req.getKey(), warehouse.getOrDefault(req.getKey(), 0L) - req.getValue() * batchSize);
        }
        warehouse.put(chemical, warehouse.getOrDefault(chemical, 0L) + reaction.getQuantity() * batchSize);
        return Math.abs(warehouse.get("ORE"));
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
