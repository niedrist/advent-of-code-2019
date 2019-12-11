package days;

import util.IntCodeComputer;


public class Day5 {

    public static void main(String[] args) {

        IntCodeComputer intCodeComputer = new IntCodeComputer();
        int[] intCodeProgram = IntCodeComputer.readIntCodeProgram();

        //Provide 1 to the input of the program to solve part 1
        //Provide 5 to the input of the program to solve part 2
        intCodeComputer.setIntCodeProgram(intCodeProgram);
        intCodeComputer.runIntCode();
    }
}

