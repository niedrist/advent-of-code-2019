package days;

import java.util.*;

import util.IntCodeComputer;

public class Day11 {

    private static Map<Character, List<Character>> directions = new HashMap<>();

    public static void main(String[] args) {
        setDirections();
        IntCodeComputer intCodeComputer = getIntCodeComputer();
        partOne(intCodeComputer);
        intCodeComputer = getIntCodeComputer();
        partTwo(intCodeComputer);
    }

    public static void partOne(IntCodeComputer intCodeComputer) {
        Map<Position, Integer> colorPositions = new HashMap<>();
        runPaintingRobot(intCodeComputer, colorPositions);
        System.out.println(colorPositions.size());
    }

    public static void partTwo(IntCodeComputer intCodeComputer) {
        Map<Position, Integer> colorPositions = new HashMap<>();
        colorPositions.put(new Position(0, 0), 1);
        runPaintingRobot(intCodeComputer, colorPositions);
        printPainting(colorPositions);
    }

    public static void runPaintingRobot(IntCodeComputer intCodeComputer, Map<Position, Integer> colorPositions) {
        int x = 0, y = 0;
        char direction = 'u';
        long intCodeOutput;
        byte outputOperation = 0;
        while (true) {
            Position currentPosition = new Position(x, y);
            if (intCodeComputer.requiresInput()) {
                long intCodeInput;
                if (colorPositions.containsKey(currentPosition))
                    intCodeInput = colorPositions.get(currentPosition);
                else 
                    intCodeInput = 0;
                intCodeComputer.setInput(intCodeInput);
            }
            intCodeComputer.nextInstruction();            
            if (intCodeComputer.hasOutput()) {
                intCodeOutput = intCodeComputer.getOutput();
                if (outputOperation == 0) {
                    colorPositions.put(currentPosition, (int) intCodeOutput);
                    outputOperation = 1;
                } else if (outputOperation == 1) {
                    direction = directions.get(direction).get((int) intCodeOutput);
                    switch(direction) {
                        case 'u':
                            y--;
                            break;
                        case 'l':
                            x--;
                            break;
                        case 'd':
                            y++;
                            break;
                        case 'r':
                            x++;
                            break;
                    }
                    outputOperation = 0;
                }
            }
            if (intCodeComputer.isFinished())
                break;
        }
    }

    public static void printPainting(Map<Position, Integer> colorPositions) {
        int minX, minY, maxX, maxY;
        maxX = maxY = Integer.MIN_VALUE;
        minX = minY = Integer.MAX_VALUE;
        for (Position p: colorPositions.keySet()) {
            if (p.getX() > maxX)
                maxX = p.getX();
            if (p.getY() > maxY)
                maxY = p.getY();
            if (p.getX() < minX)
                minX = p.getX();
            if (p.getY() < minY)
                minY = p.getY();
        }
        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                Position p = new Position(j, i);
                if (colorPositions.containsKey(p))
                    System.out.print(colorPositions.get(p) == 1 ? 'x' : ' ');
                else 
                    System.out.print(' ');
            }
            System.out.println();
        }
    }

    public static IntCodeComputer getIntCodeComputer() {
        Long[] intCodeProgram = IntCodeComputer.readIntCodeProgram();
        IntCodeComputer intCodeComputer = new IntCodeComputer(intCodeProgram);
        intCodeComputer.setInputMode(IntCodeComputer.INPUT_MODE_QUEUE);
        intCodeComputer.setOutputMode(IntCodeComputer.OUTPUT_MODE_QUEUE);
        return intCodeComputer;
    }

    public static void setDirections() {
        directions.put('u', Arrays.asList('l', 'r'));
        directions.put('l', Arrays.asList('d', 'u'));
        directions.put('r', Arrays.asList('u', 'd'));
        directions.put('d', Arrays.asList('r', 'l'));
    }

    public static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Position) {
                Position p = (Position) o;
                if (this.x == p.getX() && this.y == p.getY())
                    return true;
            }
            return false;
        }

        public int hashCode() {
            return 1000 * this.x + this.y;
        }

    }
}