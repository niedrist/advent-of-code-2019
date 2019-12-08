import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day4 {

    static final int LENGTH_PASSWORD = 6;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] inputRanges = in.nextLine().split("-");
        int startNumber = Integer.parseInt(inputRanges[0]);
        int endNumber = Integer.parseInt(inputRanges[1]);

        partOne(startNumber, endNumber);
        partTwo(startNumber, endNumber);
    }

    public static void partOne(int startNumber, int endNumber) {
        int numberPasswords = 0;
        for (int i = startNumber; i <= endNumber; i++)
            if (isValidPassword(i, false))
                numberPasswords++;

        System.out.println(numberPasswords);
    }

    public static void partTwo(int startNumber, int endNumber) {
        int numberPasswords = 0;
        for (int i = startNumber; i <= endNumber; i++)
            if (isValidPassword(i, true))
                numberPasswords++;

        System.out.println(numberPasswords);
    }

    public static boolean isValidPassword(int number, boolean maxTwoAdjacent) {
        if ((int)(Math.log10(number)+1) != LENGTH_PASSWORD)
            return false;
        int previousDigit = -1;
        boolean isAscending = true;
        boolean hasAdjacentNumbers = false;
        List<Integer> numbersAdjacentDigits = new ArrayList<>();
        int countAdjacent = 1;
        for (int j = 0; j < 6; j++) {
            int currentDigit = number % 10;
            if (previousDigit != -1 && previousDigit < currentDigit) {
                isAscending = false;
                break;
            }
            if (previousDigit == currentDigit) {
                hasAdjacentNumbers = true;
                countAdjacent++;
            } else {
                numbersAdjacentDigits.add(countAdjacent);
                countAdjacent = 1;
            }
            number /= 10;
            previousDigit = currentDigit;
        }
        numbersAdjacentDigits.add(countAdjacent);
        return (isAscending && hasAdjacentNumbers && (!maxTwoAdjacent || numbersAdjacentDigits.contains(2)));
    }
}
