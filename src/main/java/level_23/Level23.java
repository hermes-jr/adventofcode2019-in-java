package level_23;

import common.IntComp;
import common.Level;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Level23 extends Level {
    String prog;
    LinkedHashMap<Long, Queue<ImmutablePair<Long, Long>>> network = new LinkedHashMap<>();

    public Level23(String filename) {
        prog = readResourcesFirstLine(filename);
    }

    long p1() {
        IntComp[] ics = new IntComp[50];
        for (int i = 0; i < 50; i++) {
            ics[i] = new IntComp(prog, i);
            ics[i].run();
            ics[i].addToInput(i);
        }
        long result = 0;
        while (result == 0) {
            for (int i = 0; i < 50; i++) {
                IntComp cic = ics[i];
                cic.run();
                if (!cic.getOutput().isEmpty()) {
                    long recipient = Objects.requireNonNull(cic.getOutput().poll());
                    long dataX = Objects.requireNonNull(cic.getOutput().poll());
                    long dataY = Objects.requireNonNull(cic.getOutput().poll());
                    Queue<ImmutablePair<Long, Long>> sendQueue = network.getOrDefault(recipient, new LinkedList<>());
                    sendQueue.add(ImmutablePair.of(dataX, dataY));
                    network.put(recipient, sendQueue);
                    if (recipient == 255L) {
                        result = dataY;
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
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Level23 l = new Level23("input");
        System.out.println("Part1: " + l.p1());
    }
}
