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

    private Queue<Long> input = new LinkedList<>();
    private Queue<Long> output = new LinkedList<>();

    private final List<Integer> operationHasParamOne = 
        new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    private final List<Integer> operationHasParamTwo = 
        new ArrayList<>(Arrays.asList(1, 2, 5, 6, 7, 8));
    private final List<Integer> operationHasParamThree = 
        new ArrayList<>(Arrays.asList(1, 2, 7, 8));

    private HashMap<Integer, List<Integer>> modifyingParameterOperations = new HashMap<>();

    private int pointer = 0;
    private List<Long> intCodeProgram;
    private boolean isFinished = false;
    private int relativeBase = 0;

    public IntCodeComputer(Long[] intCodeProgram) {
        this.intCodeProgram = new ArrayList<Long>(Arrays.asList(intCodeProgram));

        // Each parameter for each opCode that modifies the intCodeProgram
        this.modifyingParameterOperations.put(1, Arrays.asList(3));
        this.modifyingParameterOperations.put(2, Arrays.asList());
        this.modifyingParameterOperations.put(3, Arrays.asList(1,2,7,8));
    }


    public static Long[] readIntCodeProgram() {
        String[] inputs = in.nextLine().split(",");

        Long[] intCodeProgram = new Long[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            intCodeProgram[i] = Long.parseLong(inputs[i]);

        return intCodeProgram;
    }

    public void runIntCode() {
        while ((this.intCodeProgram.get(this.pointer)) != 99) {
            nextInstruction();
        }
        this.isFinished = true;
    }

    public void nextInstruction() {
        long param1 = -1;
        long param2 = -1;
        long param3 = -1;
        int instruction = this.intCodeProgram.get(this.pointer).intValue();
        if (instruction != 99) {
            int opCode = instruction % 10;
            instruction /= 100;
            int modeParam1 = instruction % 10;
            instruction /= 10;
            int modeParam2 = instruction % 10;
            instruction /= 10;
            int modeParam3 = instruction % 10;
                
            if (operationHasParamOne.contains(opCode))
                param1 = determineParam(modeParam1, 1, opCode);
            
            if (operationHasParamTwo.contains(opCode)) 
                param2 = determineParam(modeParam2, 2, opCode);

            if (operationHasParamThree.contains(opCode)) 
                param3 = determineParam(modeParam3, 3, opCode);            

            switch (opCode) {
                case 1:
                    opCode1(param1, param2, (int) param3);
                    break;

                case 2:
                    opCode2(param1, param2, (int) param3);
                    break;

                case 3:
                    opCode3((int) param1);
                    break;

                case 4:
                    opCode4(param1);
                    break;

                case 5:
                    opCode5(param1, (int) param2);
                    break;

                case 6:
                    opCode6(param1, (int) param2);
                    break;

                case 7:
                    opCode7(param1, param2, (int) param3);
                    break;

                case 8:
                    opCode8(param1, param2, (int) param3);
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

    private void opCode1(long firstToAdd, long secondToAdd, int posToModify) {
        this.set(posToModify, (firstToAdd + secondToAdd));
        this.pointer += 4;
    }

    private void opCode2(long firstToMul, long secondToMul, int posToModify) {
        this.set(posToModify, (firstToMul * secondToMul));
        this.pointer += 4;
    }

    private void opCode3(int posToModify) {
        long newValue;
        switch (this.inputMode) {
            case INPUT_MODE_CONSOLE:
                System.out.println("Expecting Input for OpCode 3: ");
                newValue = in.nextLong();
                break;
            case INPUT_MODE_QUEUE:
                newValue = this.getInput();
                break;
            default:
                newValue = Integer.MIN_VALUE;
        }

        this.set(posToModify, newValue);
        this.pointer += 2;
    }

    private void opCode4(Long valueToPrint) {
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

    private void opCode5(long valToCheck, int pointerPos) {
        if (valToCheck != 0L)
            this.pointer = pointerPos;
        else
            this.pointer += 3;
    }
    private void opCode6(long valToCheck, int pointerPos) {
        if (valToCheck == 0L)
            this.pointer = pointerPos;
        else
            this.pointer += 3;
    }

    private void opCode7(long firstToCheck, long secondToCheck, int posToModify) {
        if (firstToCheck < secondToCheck)
            this.set(posToModify, 1L);
        else
            this.set(posToModify, 0L);
        this.pointer += 4;
    }

    private void opCode8(long firstToCheck, long secondToCheck, int posToModify) {
        if (firstToCheck == secondToCheck)
            this.set(posToModify, 1L);
        else
            this.set(posToModify, 0L);
        this.pointer += 4;
    }

    private void opCode9(long relativeBaseChange) {
        this.relativeBase += relativeBaseChange;
        this.pointer += 2;
    }

    private long determineParam(int modeParam, int numberParameter, int opCode) {
        long param;
        switch(modeParam) {
            case PARAMETER_MODE_POSITION:
                param = this.get(this.pointer + numberParameter);
                if (!this.modifyingParameterOperations.get(numberParameter).contains(opCode))
                    param = this.get((int) param);
                break;
            case PARAMETER_MODE_IMMEDIATE:
                param = this.get(this.pointer + numberParameter);
                break;
            case PARAMETER_MODE_RELATIVE:
                param = this.get(this.pointer + numberParameter);
                if (!this.modifyingParameterOperations.get(numberParameter).contains(opCode))
                    param = this.get(this.relativeBase + (int) param);
                else 
                    param = this.relativeBase + param;
                break;
            default:
                System.out.println(modeParam + " " + numberParameter);
                param = Long.MIN_VALUE;
                break;
        }
        return param;
    }

    public void set(int posToModify, long value) {
        this.extendIntCodeProgram(posToModify);
        this.intCodeProgram.set(posToModify, value);
    }

    public Long get(int index) {
        this.extendIntCodeProgram(index);
        return this.intCodeProgram.get(index);
    }

    private void extendIntCodeProgram(int index) {
        if (this.intCodeProgram.size() <= index) 
            for (int i = this.intCodeProgram.size(); i <= index; i++)
                this.intCodeProgram.add(0L);
    }

    public void setInput(long input) {
        this.input.add(input);
    }

    public long getInput() {
        if (this.input.size() != 0)
            return this.input.remove();
        return Long.MIN_VALUE;
    }

    public void setOutput(long output) {
        this.output.add(output);
    }

    public long getOutput() {
        if (this.output.size() != 0)
            return this.output.remove();
        return Long.MIN_VALUE;
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

    private IntCodeComputer() {} //keep standard constructor unaccessable
}
