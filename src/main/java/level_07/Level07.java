package level_07;

import common.IntComp;
import common.Level;

import java.util.*;

public class Level07 extends Level {
    String initialState;

    void parseData(String indata) {
        initialState = indata;
    }

    long p1() {
        long maxResult = Long.MIN_VALUE;

        for (List<Integer> perm : generatePerm(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)))) {
            long res = calcForPhase(new LinkedList<>(perm));
            if (res >= maxResult) {
                maxResult = res;
            }
        }
        return maxResult;
    }

    long p2() {
        long maxResult = Integer.MIN_VALUE;

        for (List<Integer> perm : generatePerm(new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9)))) {
            long res = calcForPhaseCyclic(new LinkedList<>(perm));
            if (res >= maxResult) {
                maxResult = res;
            }
        }
        return maxResult;
    }

    long calcForPhase(List<Integer> phase) {
        Long nextInput = 0L;
        IntComp[] amps = new IntComp[phase.size()];
        for (int i = 0; i < phase.size(); i++) {
            amps[i] = new IntComp(initialState, i);
        }
        for (int i = 0; i < phase.size(); i++) {
            amps[i].addToInput(phase.get(i));
            amps[i].addToInput(Objects.requireNonNull(nextInput));
            IntComp.ReturnReason ampResult = amps[i].run();
            if (ampResult.equals(IntComp.ReturnReason.HALTED) && amps[i].getOutput().peek() != null) {
                nextInput = amps[i].getOutput().poll();
            }
        }
        if (nextInput == null) {
            throw new RuntimeException("Unexpected result of program execution");
        }
        return nextInput;
    }

    Long calcForPhaseCyclic(List<Integer> phase) {
        Queue<Long> nextInput = new LinkedList<>();
        IntComp[] amps = new IntComp[phase.size()];
        for (int i = 0; i < phase.size(); i++) {
            amps[i] = new IntComp(initialState, i);
            amps[i].addToInput(phase.get(i));
            if (i == 0) {
                amps[i].addToInput(0);
            }
        }
        int iter = 0;

        while (true) {
            IntComp currentAmp = amps[iter % (phase.size())];
            nextInput.forEach(currentAmp::addToInput);
            nextInput.clear();
            IntComp.ReturnReason ampResult = currentAmp.run();

            if (currentAmp.getId() + 1 == phase.size() && ampResult.equals(IntComp.ReturnReason.HALTED)) {
                if (currentAmp.getOutput().peek() == null) {
                    throw new RuntimeException("Halted on empty result. Something is wrong");
                }
                return currentAmp.getOutput().poll();
            }
            nextInput = currentAmp.getOutput();
            iter++;
        }
    }

    public <E> List<List<E>> generatePerm(List<E> original) {
        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    public static void main(String[] args) {
        Level07 l = new Level07();
        String in = readResourcesFirstLine(Level07.class, "input");
        l.parseData(in);
        System.out.println("Part1: " + l.p1());
        System.out.println("Part2: " + l.p2());
    }

}
