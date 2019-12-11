package days;

import java.util.*;

public class Day6 {

    private static HashMap<String, ArrayList<String>> orbits;

    public static void main(String[] args) {
        orbits = populateHashMap();
        partOne();
        partTwo();
    }

    public static void partOne() {
        int numberOrbits = calculateNumberOrbits("COM",  1);
        System.out.println(numberOrbits);
    }

    public static void partTwo() {
        ArrayList<String> pathToYou = getPathTo("COM", "YOU");
        ArrayList<String> pathToSanta = getPathTo("COM", "SAN");
        int totalPathLength = pathToYou.size() + pathToSanta.size();
        for (int i = 0; i < pathToYou.size(); i++) {
            if (pathToYou.get(i).equals(pathToSanta.get(i)))
                totalPathLength -= 2;
            else
                break;
        }
        System.out.println(totalPathLength);
    }

    public static ArrayList<String> getPathTo(String objectFrom, String objectTo) {
        Stack<String> objectStack = new Stack<>();
        findObject(objectFrom, objectTo, objectStack);
        ArrayList<String> path = new ArrayList<>();
        while (!objectStack.isEmpty())
            path.add(objectStack.pop());
        return path;
    }

    public static boolean findObject(String objectFrom, String objectTo, Stack<String> s) {
        if (!orbits.containsKey(objectFrom))
            return false;
        if (orbits.get(objectFrom).contains(objectTo)) {
            s.push(objectFrom);
            return true;
        }
        for (String object: orbits.get(objectFrom)) {
            if (findObject(object, objectTo, s)) {
                s.push(objectFrom);
                return true;
            }
        }
        return false;
    }

    public static int calculateNumberOrbits(String center, int depth) {
        int numberOrbits = 0;
        if (orbits.containsKey(center)) {
            ArrayList<String> orbitingObjects = orbits.get(center);
            for (String orbitingObject : orbitingObjects)
                numberOrbits += depth + calculateNumberOrbits(orbitingObject, depth + 1);
        }
        return numberOrbits;
    }

    public static HashMap<String, ArrayList<String>> populateHashMap() {
        Scanner in = new Scanner(System.in);
        HashMap<String, ArrayList<String>> orbits = new HashMap<>();
        String input;
        while (!(input = in.nextLine()).equals(";") ) {
            String[] objects = input.split("\\)");
            ArrayList<String> outerObjects;
            if (orbits.containsKey(objects[0])) {
                outerObjects = orbits.get(objects[0]);
            } else {
                outerObjects = new ArrayList<>();
                orbits.put(objects[0], outerObjects);
            }
            outerObjects.add(objects[1]);
        }
        return orbits;
    }
}
