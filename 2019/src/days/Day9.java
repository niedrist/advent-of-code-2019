package days;

import java.util.*;
import util.IntCodeComputer;

public class Day9 {
    public static void main(String[] args) {
        Long[] intCodeProgram = IntCodeComputer.readIntCodeProgram();
        IntCodeComputer intCodeComputer = new IntCodeComputer(intCodeProgram);
        //Provide 1 to solve part one, provide 2 to solve part 2
        intCodeComputer.runIntCode();
    }
}