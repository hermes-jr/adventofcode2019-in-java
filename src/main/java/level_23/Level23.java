package level_23;

import common.IntComp;
import common.Level;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class Level23 extends Level {
    String prog;
    Map<Long, Queue<ImmutablePair<Long, Long>>> network = new HashMap<>();
    ImmutablePair<Long, Long> lastNatPacket = null;
    Set<Long> seenByNat = new HashSet<>();

    public Level23(String filename) {
        prog = readResourcesFirstLine(filename);
    }

    void networkGo() {
        IntComp[] ics = new IntComp[50];
        for (int i = 0; i < 50; i++) {
            ics[i] = new IntComp(prog, i);
            ics[i].run();
            ics[i].addToInput(i);
        }
        long result = -1;
        while (true) {
            boolean idle = true;
            for (int i = 0; i < 50; i++) {
                IntComp cic = ics[i];
                cic.run();
                if (!cic.getOutput().isEmpty()) {
                    long recipient = Objects.requireNonNull(cic.getOutput().poll());
                    long left = Objects.requireNonNull(cic.getOutput().poll());
                    long right = Objects.requireNonNull(cic.getOutput().poll());
                    Queue<ImmutablePair<Long, Long>> sendQueue = network.getOrDefault(recipient, new LinkedList<>());
                    sendQueue.add(ImmutablePair.of(left, right));
                    network.put(recipient, sendQueue);
                    idle = false;
                    if (recipient == 255L) {
                        lastNatPacket = ImmutablePair.of(left, right);
//                        seenByNat.add(right);
                        if (result == -1) {
                            result = right;
                            System.out.println("Part1: " + result);
                        }
                    }
                }
                Queue<ImmutablePair<Long, Long>> forMe = network.getOrDefault((long) i, new LinkedList<>());
                if (forMe.isEmpty()) {
                    cic.addToInput(-1L);
                } else {
                    while (!forMe.isEmpty()) {
                        ImmutablePair<Long, Long> val = forMe.poll();
                        cic.addToInput(val.getLeft());
                        cic.addToInput(val.getRight());
                    }
                    idle = false;
                }
            }
            if (idle && lastNatPacket != null) {
                Queue<ImmutablePair<Long, Long>> startPacket = new LinkedList<>();
                startPacket.add(lastNatPacket);
                if (seenByNat.contains(lastNatPacket.getRight())) {
                    System.out.println("Part2: " + lastNatPacket.getRight());
                    break;
                }
                seenByNat.add(lastNatPacket.getRight());
                network.put(0L, startPacket);
            }
        }
    }

    public static void main(String[] args) {
        Level23 l = new Level23("input");
        l.networkGo();
    }
}
