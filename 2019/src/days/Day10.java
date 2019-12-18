package days;

import java.util.*;


public class Day10 {
    public static void main(String[] args) {
        char[][] asteriodField = scanAsteriodField();
        
        partOne(asteriodField);
    }

    public static void partOne(char[][] asteriodField) {
        int maxObservableAsteroids = -1;
        int observableAsteroids = -1;
        for (int i = 0; i < asteriodField.length; i++)
            for (int j = 0; j < asteriodField[i].length; j++) {
                if (asteriodField[i][j] == '#' && 
                    (observableAsteroids = determineObservableAsteroids(i, j, asteriodField)) > maxObservableAsteroids) {
                    maxObservableAsteroids = observableAsteroids;
                }
            }
        System.out.println(maxObservableAsteroids);
    }

    public static int determineObservableAsteroids(int x, int y, char[][] asteriodField) {
        int observableAsteroids = 0;
        List<Vector<Integer>> blockedVectors = new ArrayList<>();
        for (int i = 0; i < asteriodField.length; i++)
            for (int j = 0; j < asteriodField[i].length; j++) {
                if (asteriodField[i][j] == '#' && !(i == x && j == y)) {
                    Vector<Integer> minVector = calculateMinVector(i - x, j - y);
                    if (!blockedVectors.contains(minVector)) {
                        blockedVectors.add(minVector);
                        observableAsteroids++;
                    }
                }
            }
        return observableAsteroids;
    }

    public static Vector<Integer> calculateMinVector(int x, int y) {
        int gcd = Math.abs(gcd(x, y));
        Vector<Integer> minVector = new Vector<>();
        minVector.add(x / gcd);
        minVector.add(y / gcd);
        return minVector;
    }

    public static int gcd(int a, int b) {
        if (b==0)
            return a;
        return gcd(b,a%b);
     }

    public static char[][] scanAsteriodField() {
        Scanner in = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        String line;
        while (true) {
            line = in.nextLine();
            if (line.equals(";"))
                break;
            lines.add(line);
        }
        
        char[][] asteriodField = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) 
            asteriodField[i] = lines.get(i).toCharArray();

        // Transpose matrix to get coordinates right, maybe refactor later to a more 
        // efficient solution
        for (int i = 0; i < asteriodField.length; i++)
            for (int j = i + 1; j < asteriodField[i].length; j++) {
                char tmp = asteriodField[i][j];
                asteriodField[i][j] = asteriodField[j][i];
                asteriodField[j][i] = tmp;
            }
        return asteriodField;
    }
}