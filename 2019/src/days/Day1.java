package days;

import java.util.*;

public class Day1 {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        List<Integer> masses = new ArrayList<>();
        while (in.hasNextInt())
            masses.add(in.nextInt());

        partOne(masses);
        partTwo(masses);
    }

    public static void partOne(List<Integer> masses) {
        int sum = 0;
        for (int mass: masses)
            sum += (mass / 3) - 2;
        System.out.println(sum);
    }

    public static void partTwo(List<Integer> masses) {
        int sum = 0;
        for (int mass: masses) {
            while ((mass = (mass / 3) - 2) > 0)
                sum += mass;
        }
        System.out.println(sum);
    }
}
