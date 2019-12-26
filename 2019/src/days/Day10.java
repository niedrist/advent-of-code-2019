package days;

import java.util.*;

public class Day10 {
    public static void main(String[] args) {
        char[][] asteroidField = scanAsteriodField();
        
        Asteroid bestAsteroid = partOne(asteroidField);
        partTwo(bestAsteroid, asteroidField);
    }

    public static Asteroid partOne(char[][] asteroidField) {
        Asteroid bestAsteroid = null;
        for (int i = 0; i < asteroidField.length; i++)
            for (int j = 0; j < asteroidField[i].length; j++) {
                if (asteroidField[i][j] == '#') {
                    Asteroid asteroid = new Asteroid(i, j);
                    asteroid.determineObservableAsteroids(asteroidField);
                    if (bestAsteroid == null || bestAsteroid.observableAsteroids < asteroid.observableAsteroids)
                        bestAsteroid = asteroid;
                }
            }
        System.out.println(bestAsteroid.observableAsteroids);
        return bestAsteroid;
    }

    // TODO Part two needs a rework (simplify used datastructures maybe?)
    public static void partTwo(Asteroid laserAsteroid, char[][] asteroidField) {
        Map<Double, List<Vector<Integer>>> angleAsteroids = getAngleAsteroids(laserAsteroid, asteroidField);
        sortAngleAsteroids(angleAsteroids);
        Vector<Integer> lastAsteroid = getLastAsteroid(angleAsteroids);
        System.out.println((lastAsteroid.get(0)+laserAsteroid.x)*100 + (lastAsteroid.get(1)+laserAsteroid.y));
        
    }

    public static Vector<Integer> getLastAsteroid(Map<Double, List<Vector<Integer>>> angleAsteroids) {
        List<List<Vector<Integer>>> angleAsteroidsList = new ArrayList<>(angleAsteroids.values());
        int asteroidsToDestroy = 200;
        Vector<Integer> lastAsteroid = null;
        int i = 0;
        for (Double ang: angleAsteroids.keySet())
            if (ang < 90)
                i++;
        while (!angleAsteroidsList.isEmpty()){
            if (i <= 0 )
                i = angleAsteroidsList.size() - 1;
            List<Vector<Integer>> list = angleAsteroidsList.get(i);
            asteroidsToDestroy--;
            if (asteroidsToDestroy == 1) {
                lastAsteroid = list.get(0);
                break;
            }
            list.remove(0);
            if (list.isEmpty())
                angleAsteroidsList.remove(list);
            i--;
        }
        return lastAsteroid;
    }

    public static double calculateAngle(int x, int y) {
        return ((Math.atan2(-y, x) / Math.PI * 180) +  360) % 360;
    }

    public static Map<Double, List<Vector<Integer>>> 
                getAngleAsteroids(Asteroid laserAsteroid, char[][] asteroidField) {
        Map<Double, List<Vector<Integer>>> angleAsteroids = new TreeMap<>();
        for (int i = 0; i < asteroidField.length; i++)
            for (int j = 0; j < asteroidField[i].length; j++) {
                if (asteroidField[i][j] == '#') {
                    Vector<Integer> asteroid = new Vector<>();
                    int xRelative = i - laserAsteroid.x;
                    int yRelative = j - laserAsteroid.y;
                    asteroid.add(xRelative);
                    asteroid.add(yRelative);
                    double angle = calculateAngle(xRelative, yRelative);
                    List<Vector<Integer>> asteroidsAtAngle;

                    if (angleAsteroids.containsKey(angle))
                        asteroidsAtAngle = angleAsteroids.get(angle);
                    else
                        asteroidsAtAngle = new ArrayList<>();

                    asteroidsAtAngle.add(asteroid);
                    angleAsteroids.put(angle, asteroidsAtAngle);
                }
            }
        return angleAsteroids;
    }

    public static void sortAngleAsteroids(Map<Double, List<Vector<Integer>>> angleAsteroids) {
        for (List<Vector<Integer>> l: angleAsteroids.values()) 
            Collections.sort(l, new Comparator<Vector<Integer>>() {
                public int compare(Vector<Integer> v1, Vector<Integer> v2) {
                    double distanceV1 = Math.sqrt(Math.pow(v1.get(0), 2) + Math.pow(v1.get(0), 2));
                    double distanceV2 = Math.sqrt(Math.pow(v2.get(0), 2) + Math.pow(v2.get(0), 2));
                    // vectors will always be distinct so there is no need to check for equality
                    return (distanceV1 - distanceV2 > 0) ? 1 : -1; 
                }
            });
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
        char[][] transposedField = new char[asteriodField[0].length][];
        for (int i = 0; i < asteriodField[0].length; i++) {
            transposedField[i] = new char[asteriodField.length];
            for (int j = 0; j < asteriodField.length; j++) {
                transposedField[i][j] = asteriodField[j][i];
            }
        }
        return transposedField;
    }

    public static class Asteroid {
        public int x;
        public int y;

        public int observableAsteroids = 0;

        public Asteroid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString(){
            return this.x + ":" + this.y + ", oA: " + this.observableAsteroids;
        } 

        public void incrementObservableAsteroids() {
            this.observableAsteroids++;
        }

        public void determineObservableAsteroids(char[][] asteriodField) {
            List<Vector<Integer>> blockedVectors = new ArrayList<>();
            for (int i = 0; i < asteriodField.length; i++)
                for (int j = 0; j < asteriodField[i].length; j++) {
                    if (asteriodField[i][j] == '#' && !(i == this.x && j == this.y)) {
                        Vector<Integer> minVector = calculateMinVector(i - this.x, j - this.y);
                        if (!blockedVectors.contains(minVector)) {
                            blockedVectors.add(minVector);
                            this.observableAsteroids++;
                        }
                    }
                }
        }

    }
}