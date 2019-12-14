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

    private final int PARAMETER_MODE_POSITION = 0;
    private final int PARAMETER_MODE_IMMEDIATE = 1;
    private final int PARAMETER_MODE_RELATIVE = 2;

    private Queue<Integer> input = new LinkedList<>();
    private Queue<Integer> output = new LinkedList<>();

    private final Integer[] operationsParamOne = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private final Integer[] operationsParamTwo = {1, 2, 5, 6, 7, 8};
    private final Integer[] operationsParamThree = {1, 2, 7, 8};

    private final List<Integer> operationHasParamOne = new ArrayList<>(Arrays.asList(operationsParamOne));
    private final List<Integer> operationHasParamTwo = new ArrayList<>(Arrays.asList(operationsParamTwo));
    private final List<Integer> operationHasParamThree = new ArrayList<>(Arrays.asList(operationsParamThree));

    public int pointer = 0;
    private int[] intCodeProgram = null;
    private boolean isFinished = false;
    private int relativeBase = 0;


    public static int[] readIntCodeProgram() {
        String[] inputs = in.nextLine().split(",");

        int[] intCodeProgram = new int[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            intCodeProgram[i] = Integer.parseInt(inputs[i]);

        return intCodeProgram;
    }

    public void runIntCode() {
        if (this.intCodeProgram == null) {
            System.out.println("No program provided");
            return;
        }
        while ((this.intCodeProgram[this.pointer]) != 99) {
            nextInstruction();
        }
        this.isFinished = true;

    }

    public void nextInstruction() {
        int param1 = -1;
        int param2 = -1;
        int param3 = -1;
        int instruction = this.intCodeProgram[this.pointer];
        if (instruction != 99) {
            int opCode = instruction % 10;
            instruction /= 100;
            int modeParam1 = instruction % 10;
            instruction /= 10;
            int modeParam2 = instruction % 10;

            int modeParam3 = PARAMETER_MODE_IMMEDIATE; //always immeidate mode because always write-only

            // Param 1 is write-only for Opcode 3
            if (opCode == 3)
                modeParam1 = PARAMETER_MODE_IMMEDIATE;

            if (operationHasParamOne.contains(opCode))
                param1 = determineParam(modeParam1, 1);
            
            if (operationHasParamTwo.contains(opCode)) 
                param2 = determineParam(modeParam2, 2);

            if (operationHasParamThree.contains(opCode)) 
                param3 = determineParam(modeParam3, 3);
            

            switch (opCode) {
                case 1:
                    opCode1(param1, param2, param3);
                    break;

                case 2:
                    opCode2(param1, param2, param3);
                    break;

                case 3:
                    opCode3(param1);
                    break;

                case 4:
                    opCode4(param1);
                    break;

                case 5:
                    opCode5(param1, param2);
                    break;

                case 6:
                    opCode6(param1, param2);
                    break;

                case 7:
                    opCode7(param1, param2, param3);
                    break;

                case 8:
                    opCode8(param1, param2, param3);
                    break;

                case 9:
                    opCode9(param1);
                    break;

                default:
                    System.out.println("Unsupported OP code: " + opCode);
                    return;
            }
        } else {
            this.isFinished = true;
        }
    }

    private void opCode1(int firstToAdd, int secondToAdd, int posToModify) {
        this.intCodeProgram[posToModify] = firstToAdd + secondToAdd;
        this.pointer += 4;
    }

    private void opCode2(int firstToMul, int secondToMul, int posToModify) {
        this.intCodeProgram[posToModify] = firstToMul * secondToMul;
        this.pointer += 4;
    }

    private void opCode3(int posToModify) {
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

        this.intCodeProgram[posToModify] = newValue;
        this.pointer += 2;
    }

    private void opCode4(int valueToPrint) {
        switch(this.outputMode) {
            case OUTPUT_MODE_CONSOLE:
                System.out.println(valueToPrint);
                break;
            case OUTPUT_MODE_QUEUE:
                this.setOutput(valueToPrint);
                break;
        }
        this.pointer += 2;
    }

    private void opCode5(int valToCheck, int pointerPos) {
        if (valToCheck != 0)
            this.pointer = pointerPos;
        else
            this.pointer += 3;
    }
    private void opCode6(int valToCheck, int pointerPos) {
        if (valToCheck == 0)
            this.pointer = pointerPos;
        else
            this.pointer += 3;
    }

    private void opCode7(int firstToCheck, int secondToCheck, int posToModify) {
        if (firstToCheck < secondToCheck)
            this.intCodeProgram[posToModify] = 1;
        else
            this.intCodeProgram[posToModify] = 0;
        this.pointer += 4;
    }

    private void opCode8(int firstToCheck, int secondToCheck, int posToModify) {
        if (firstToCheck == secondToCheck)
            this.intCodeProgram[posToModify] = 1;
        else
            this.intCodeProgram[posToModify] = 0;
        this.pointer += 4;
    }

    private void opCode9(int relativeBaseChange) {
        this.relativeBase += relativeBaseChange;
        this.pointer += 2;
    }

    private int determineParam(int modeParam, int numberParameter) {
        int param;
        switch(modeParam) {
            case PARAMETER_MODE_POSITION:
                param = this.intCodeProgram[this.intCodeProgram[this.pointer + numberParameter]];
                break;
            case PARAMETER_MODE_IMMEDIATE:
                param = this.intCodeProgram[this.pointer + numberParameter];
                break;
            case PARAMETER_MODE_RELATIVE:
                param = this.intCodeProgram[this.relativeBase];
                break;
            default:
                System.out.println(modeParam + " " + numberParameter);
                param = Integer.MIN_VALUE;
                break;
        }
        return param;
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

    public boolean isFinished() {
        return isFinished;
    }
}
