package days;

import util.IntCodeComputer;
import util.Permutation;

import java.util.ArrayList;
import java.util.List;

public class Day7 {

    private static int NUM_AMPLIFIERS = 5;

    public static void main(String[] args) {

        Long[] intCodeProgram = IntCodeComputer.readIntCodeProgram();

        partOne(intCodeProgram.clone());
        partTwo(intCodeProgram.clone());
    }

    public static void partOne(Long[] intCodeProgram) {

        int[] settingSequenceNumbers = new int[]{0, 1, 2, 3, 4};
        List<List<Integer>> permutations = Permutation.permute(settingSequenceNumbers);
        int maxSignal = Integer.MIN_VALUE;
        for (List<Integer> permutation : permutations) {
            int thrusterSignal = getThrusterSignal(intCodeProgram, permutation);
            if (thrusterSignal > maxSignal)
                maxSignal = thrusterSignal;
        }
        System.out.println(maxSignal);
    }

    public static void partTwo(Long[] intCodeProgram) {

        int[] settingSequenceNumbers = new int[]{5, 6, 7, 8, 9};
        List<List<Integer>> permutations = Permutation.permute(settingSequenceNumbers);
        int maxSignal = Integer.MIN_VALUE;
        for (List<Integer> permutation : permutations) {
            int thrusterSignal = getFeedbackLoopThrusterSignal(intCodeProgram, permutation);
            if (thrusterSignal > maxSignal)
                maxSignal = thrusterSignal;
        }
        System.out.println(maxSignal);
    }


    public static int getThrusterSignal(Long[] intCodeProgram, List<Integer> settingSequence) {
        int currentSignal = 0;
        for (int amplifier = 0; amplifier < NUM_AMPLIFIERS; amplifier++) {
            IntCodeComputer comp = new IntCodeComputer(intCodeProgram.clone());
            comp.setInputMode(IntCodeComputer.INPUT_MODE_QUEUE);
            comp.setOutputMode(IntCodeComputer.OUTPUT_MODE_QUEUE);
            comp.setInput(settingSequence.get(amplifier));
            comp.setInput(currentSignal);
            comp.runIntCode();
            currentSignal = (int) comp.getOutput();
        }
        return currentSignal;
    }

    public static int getFeedbackLoopThrusterSignal(Long[] intCodeProgram, List<Integer> settingSequence) {
        int currentSignal = 0;
        List<IntCodeComputer> computers = new ArrayList<>();
        for (int amplifier = 0; amplifier < NUM_AMPLIFIERS; amplifier++) {
            IntCodeComputer comp = new IntCodeComputer(intCodeProgram.clone());
            comp.setInputMode(IntCodeComputer.INPUT_MODE_QUEUE);
            comp.setOutputMode(IntCodeComputer.OUTPUT_MODE_QUEUE);
            comp.setInput(settingSequence.get(amplifier));
            computers.add(comp);
        }
        int amplifier = 0;
        while (true) {
            boolean allFinished = true;
            for (IntCodeComputer comp: computers)
                allFinished = allFinished && comp.isFinished();

            if (allFinished)
                break;

            IntCodeComputer currentAmplifier = computers.get(amplifier);
            currentAmplifier.setInput(currentSignal);
            long testSignal;
            while ((testSignal = currentAmplifier.getOutput()) == Long.MIN_VALUE) {
                currentAmplifier.nextInstruction();
                if (currentAmplifier.isFinished())
                    break;
            }

            if (testSignal != Long.MIN_VALUE)
                currentSignal = (int) testSignal;

            amplifier = (amplifier + 1) % NUM_AMPLIFIERS;
        }
        return currentSignal;
    }


}
