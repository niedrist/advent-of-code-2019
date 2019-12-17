package days;

import java.util.*;
import util.IntCodeComputer;

public class Day9 {
    public static void main(String[] args) {
        Long[] intCodeProgram = IntCodeComputer.readIntCodeProgram();
        IntCodeComputer intCodeComputer = new IntCodeComputer(intCodeProgram);
        intCodeComputer.runIntCode();
    }
}