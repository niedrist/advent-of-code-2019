package days;

import java.util.Scanner;

public class Day2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] inputs = in.nextLine().split(",");
        int[] intCodeProgram = new int[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            intCodeProgram[i] = Integer.parseInt(inputs[i]);

        partOne(intCodeProgram.clone());
        partTwo(intCodeProgram.clone());
    }

    public static void partOne(int[] intCodeProgram) {
        intCodeProgram[1] = 12;
        intCodeProgram[2] = 2;
        runIntCode(intCodeProgram);
        System.out.println(intCodeProgram[0]);
    }

    public static void partTwo(int[] intCodeProgram) {
        for (int i = 0; i <= 99; i ++)
            for (int j = 0; j <= 99; j++) {
                intCodeProgram[1] = i;
                intCodeProgram[2] = j;
                int[] copyProgram = intCodeProgram.clone();
                runIntCode(copyProgram);
                if (copyProgram[0] == 19690720) {
                    System.out.println(100 * i + j);
                    return;
                }
            }
    }

    public static void runIntCode(int[]intCodeProgram) {
        int pointer = 0;
        while(intCodeProgram[pointer] != 99) {
            int posToOp1 = intCodeProgram[pointer + 1];
            int posToOp2 = intCodeProgram[pointer + 2];
            int posToModify = intCodeProgram[pointer + 3];
            switch(intCodeProgram[pointer]) {
                case 1:
                    intCodeProgram[posToModify] = intCodeProgram[posToOp1] + intCodeProgram[posToOp2];
                    break;
                case 2:
                    intCodeProgram[posToModify] = intCodeProgram[posToOp1] * intCodeProgram[posToOp2];
                    break;
            }
            pointer += 4;
        }
    }

}
