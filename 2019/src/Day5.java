import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day5 {

    private static final Scanner in = new Scanner(System.in);

    private static final Integer[] operationsParamOne = {1,2,3,4,5,6,7,8};
    private static final Integer[] operationsParamTwo = {1,2,5,6,7,8};
    private static final Integer[] operationsParamThree = {1,2,7,8};

    private static final List<Integer> operationHasParamOne = new ArrayList<>(Arrays.asList(operationsParamOne));
    private static final List<Integer> operationHasParamTwo = new ArrayList<>(Arrays.asList(operationsParamTwo));
    private static final List<Integer> operationHasParamThree = new ArrayList<>(Arrays.asList(operationsParamThree));

    public static void main(String[] args) {
        String[] inputs = in.nextLine().split(",");
        int[] intCodeProgram = new int[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            intCodeProgram[i] = Integer.parseInt(inputs[i]);

        //Provide 1 to the input of the program to solve part 1
        //Provide 5 to the input of the program to solve part 2
        runIntCode(intCodeProgram);
    }


    public static void runIntCode(int[] intCodeProgram) {
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
                    int newValue = in.nextInt();
                    intCodeProgram[param1] = newValue;
                    pointer += 2;
                    break;

                case 4:
                    System.out.println(param1);
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
                    System.out.println("Unsupported OP code: "+opCode);
                    return;
            }
        }
    }
}

