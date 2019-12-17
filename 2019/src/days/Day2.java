package days;

import java.util.Scanner;
import util.IntCodeComputer;

public class Day2 {
    public static void main(String[] args) {
        Long[] intCodeProgram = IntCodeComputer.readIntCodeProgram();

        partOne(intCodeProgram.clone());
        partTwo(intCodeProgram.clone());
    }

    public static void partOne(Long[] intCodeProgram) {
        intCodeProgram[1] = 12L;
        intCodeProgram[2] = 2L;
        IntCodeComputer intCodeComputer = new IntCodeComputer(intCodeProgram);
        intCodeComputer.runIntCode();
        System.out.println(intCodeComputer.get(0));
    }

    public static void partTwo(Long[] intCodeProgram) {
        IntCodeComputer intCodeComputer;
        for (int i = 0; i <= 99; i ++)
            for (int j = 0; j <= 99; j++) {
                intCodeProgram[1] = (long) i;
                intCodeProgram[2] = (long) j;
                intCodeComputer = new IntCodeComputer(intCodeProgram);
                intCodeComputer.runIntCode();
                if (intCodeComputer.get(0) == 19690720) {
                    System.out.println(100 * i + j);
                    return;
                }
            }
    }

}
