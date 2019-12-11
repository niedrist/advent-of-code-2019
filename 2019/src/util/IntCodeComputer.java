package util;

import java.util.*;


public class IntCodeComputer {
    private static final Scanner in = new Scanner(System.in);

    public static final int INPUT_MODE_CONSOLE = 0;
    public static final int INPUT_MODE_QUEUE = 1;

    public static final int OUTPUT_MODE_CONSOLE = 0;
    public static final int OUTPUT_MODE_QUEUE = 1;

    private int inputMode = INPUT_MODE_CONSOLE;
    private int outputMode = OUTPUT_MODE_CONSOLE;

    private Queue<Integer> input = new LinkedList<>();
    private Queue<Integer> output = new LinkedList<>();

    private final Integer[] operationsParamOne = {1, 2, 3, 4, 5, 6, 7, 8};
    private final Integer[] operationsParamTwo = {1, 2, 5, 6, 7, 8};
    private final Integer[] operationsParamThree = {1, 2, 7, 8};

    private final List<Integer> operationHasParamOne = new ArrayList<>(Arrays.asList(operationsParamOne));
    private final List<Integer> operationHasParamTwo = new ArrayList<>(Arrays.asList(operationsParamTwo));
    private final List<Integer> operationHasParamThree = new ArrayList<>(Arrays.asList(operationsParamThree));


    public int[] readIntCodeProgram() {
        String[] inputs = in.nextLine().split(",");
        int[] intCodeProgram = new int[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            intCodeProgram[i] = Integer.parseInt(inputs[i]);

        return intCodeProgram;
    }

    public void runIntCode(int[] intCodeProgram) {
        int pointer = 0;
        int instruction;
        int param1 = -1;
        int param2 = -1;
        int param3 = -1;
        while ((instruction = intCodeProgram[pointer]) != 99) {
            int opCode = instruction % 10;
            instruction /= 100;
            boolean positionModeParam1 = instruction % 10 == 0;
            instruction /= 10;
            boolean positionModeParam2 = instruction % 10 == 0;

            // Determine Parameter 1
            if (operationHasParamOne.contains(opCode)) {
                param1 = intCodeProgram[pointer + 1];
                if (opCode != 3)
                    if (positionModeParam1)
                        param1 = intCodeProgram[param1];
            }
            //Determine Parameter 2
            if (operationHasParamTwo.contains(opCode)) {
                param2 = intCodeProgram[pointer + 2];
                if (positionModeParam2)
                    param2 = intCodeProgram[param2];
            }

            //Determine Parameter 3
            if (operationHasParamThree.contains(opCode)) {
                param3 = intCodeProgram[pointer + 3];
            }

            switch (opCode) {
                case 1:
                    intCodeProgram[param3] = param1 + param2;
                    pointer += 4;
                    break;

                case 2:
                    intCodeProgram[param3] = param1 * param2;
                    pointer += 4;
                    break;

                case 3:
                    int newValue;
                    switch (this.inputMode) {
                        case INPUT_MODE_CONSOLE:
                            System.out.println("Expecting Input for OpCode 3: ");
                            newValue = in.nextInt();
                            break;
                        case INPUT_MODE_QUEUE:
                            newValue = this.getInput();
                            break;
                        default:
                            newValue = Integer.MIN_VALUE;
                    }

                    intCodeProgram[param1] = newValue;
                    pointer += 2;
                    break;

                case 4:
                    switch(this.outputMode) {
                        case OUTPUT_MODE_CONSOLE:
                            System.out.println(param1);
                            break;
                        case OUTPUT_MODE_QUEUE:
                            this.setOutput(param1);
                            break;
                    }
                    pointer += 2;
                    break;

                case 5:
                    if (param1 != 0)
                        pointer = param2;
                    else
                        pointer += 3;
                    break;

                case 6:
                    if (param1 == 0)
                        pointer = param2;
                    else
                        pointer += 3;
                    break;

                case 7:
                    if (param1 < param2)
                        intCodeProgram[param3] = 1;
                    else
                        intCodeProgram[param3] = 0;
                    pointer += 4;
                    break;

                case 8:
                    if (param1 == param2)
                        intCodeProgram[param3] = 1;
                    else
                        intCodeProgram[param3] = 0;
                    pointer += 4;
                    break;
                default:
                    System.out.println("Unsupported OP code: " + opCode);
                    return;
            }
        }
    }

    public void setInput(int input) {
        this.input.add(input);
    }

    public int getInput() {
        if (this.input.size() != 0)
            return this.input.remove();
        return Integer.MIN_VALUE;
    }

    public void setOutput(int output) {
        this.output.add(output);
    }

    public int getOutput() {
        if (this.output.size() != 0)
            return this.output.remove();
        return Integer.MIN_VALUE;
    }

    public void setInputMode(int inputMode) {
        this.inputMode = inputMode;
    }

    public void setOutputMode(int outputMode) {
        this.outputMode = outputMode;
    }

}
