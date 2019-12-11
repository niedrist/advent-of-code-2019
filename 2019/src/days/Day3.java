package days;

import java.util.*;

public class Day3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] pathWire1 = in.nextLine().split(",");
        String[] pathWire2 = in.nextLine().split(",");

        HashMap<String, Coordinate> allCoordsWire1 = calculatePathCoords(pathWire1);
        HashMap<String, Coordinate> allCoordsWire2 = calculatePathCoords(pathWire2);

        partOne(allCoordsWire1, allCoordsWire2);
        partTwo(allCoordsWire1, allCoordsWire2);

    }

    public static void partOne(HashMap<String, Coordinate> allCoordsWire1, HashMap<String, Coordinate> allCoordsWire2) {
        int minDistance = Integer.MAX_VALUE;
        for (String coordWire1: allCoordsWire1.keySet()) {
            if (allCoordsWire2.containsKey(coordWire1)) {
                int distance = allCoordsWire1.get(coordWire1).getManhattanDistance();
                if (distance < minDistance)
                    minDistance = distance;
            }
        }
        System.out.println(minDistance);
    }

    public static void partTwo(HashMap<String, Coordinate> allCoordsWire1, HashMap<String, Coordinate> allCoordsWire2) {
        int minSteps = Integer.MAX_VALUE;
        for (String coordWire1: allCoordsWire1.keySet()) {
            if (allCoordsWire2.containsKey(coordWire1)) {
                int stepsWire1 = allCoordsWire1.get(coordWire1).getSteps();
                int stepsWire2 = allCoordsWire2.get(coordWire1).getSteps();
                int stepsTotal = stepsWire1 + stepsWire2;
                if (stepsTotal < minSteps)
                    minSteps = stepsTotal;
            }
        }
        System.out.println(minSteps);

    }

    public static HashMap<String, Coordinate> calculatePathCoords(String[] path) {
        int xPos = 0;
        int yPos = 0;
        int steps = 0;
        HashMap<String, Coordinate> allCoords = new HashMap<>();
        for (String move: path) {
            char direction = move.charAt(0);
            int distance = Integer.parseInt(move.substring(1));
            switch (direction) {
                case 'R':
                    for (int i = 1; i <= distance; i++) {
                        xPos++;
                        steps++;
                        addCoordinate(allCoords, xPos, yPos, steps);
                    }
                    break;
                case 'U':
                    for (int i = 1; i <= distance; i++) {
                        yPos--;
                        steps++;
                        addCoordinate(allCoords, xPos, yPos, steps);
                    }
                    break;
                case 'L':
                    for (int i = 1; i <= distance; i++) {
                        xPos--;
                        steps++;
                        addCoordinate(allCoords, xPos, yPos, steps);
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= distance; i++) {
                        yPos++;
                        steps++;
                        addCoordinate(allCoords, xPos, yPos, steps);
                    }
                    break;
            }
        }
        return allCoords;
    }

    public static void addCoordinate(HashMap<String, Coordinate> allCoords, int x, int y, int steps) {
        Coordinate c = new Coordinate(x, y, (Math.abs(x) + Math.abs(y)), steps);
        allCoords.put((x) + ":" + (y), c);
    }

    public static class Coordinate {
        private int xPos;
        private int yPos;
        private int manhattanDistance;
        private int steps;

        public Coordinate(int xPos, int yPos, int manhattanDistance, int steps) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.manhattanDistance = manhattanDistance;
            this.steps = steps;
        }

        public int getxPos() {
            return xPos;
        }

        public int getyPos() {
            return yPos;
        }

        public int getManhattanDistance() {
            return manhattanDistance;
        }

        public int getSteps() {
            return steps;
        }
    }
}
