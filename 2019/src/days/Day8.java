package days;

import java.util.*;

public class Day8 {

    public static final int IMAGE_WIDTH = 25;
    public static final int IMAGE_HEIGHT = 6;

    public static void main(String[] args) {
        List<List<Integer>> layers = createLayers(readInput());
        partOne(layers);
        partTwo(layers);
    }

    public static void partOne(List<List<Integer>> layers) {
        List<Integer> selectedLayer = getLayerWithFewest(0, layers);
        int solution = calculateSolutionPartOne(selectedLayer);
        System.out.println(solution);
    }

    public static void partTwo(List<List<Integer>> layers) {
        int[] finalLayer = generateFinalLayer(layers);
        printLayer(finalLayer);
    }

    public static void printLayer(int[] layer) {
        for (int i = 0; i < IMAGE_HEIGHT; i++) {
            for (int j = 0; j < IMAGE_WIDTH; j++) {
                if (layer[i * IMAGE_WIDTH + j] == 0)
                    System.out.print(" ");
                else
                    System.out.print(layer[i * IMAGE_WIDTH + j]);
            }
            System.out.println();
        }
    }

    public static int[] generateFinalLayer(List<List<Integer>> layers) {
        int[] finalLayer = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
        for (int i = 0; i < finalLayer.length; i++)
            finalLayer[i] = -1;

        for (List<Integer> layer: layers) {
            for (int i = 0; i < layer.size(); i++) {
                int digit = layer.get(i);
                if (finalLayer[i] == -1 && (digit == 0 || digit == 1))
                    finalLayer[i] = digit;
            }
        }
        return finalLayer;
    }

    public static List<Integer> getLayerWithFewest(int digitToCheck, List<List<Integer>> layers) {
        List<Integer> selectedLayer = null;
        int digitsSelectedLayer = Integer.MAX_VALUE;
        for (int i = 0; i < layers.size(); i++) {
            List<Integer> currentLayer = layers.get(i);
            int digitToCheckCounter = 0;
            for (int digit: currentLayer)
                if (digit == digitToCheck)
                    digitToCheckCounter++;
            if (digitToCheckCounter < digitsSelectedLayer) {
                selectedLayer = currentLayer;
                digitsSelectedLayer = digitToCheckCounter;
            }
        }
        return selectedLayer;
    }

    public static int calculateSolutionPartOne(List<Integer> layer) {
        int counterOne = 0;
        int counterTwo = 0;
        for (int digit: layer) {
            if (digit == 1)
                counterOne++;
            if (digit == 2)
                counterTwo++;
        }
        return counterOne * counterTwo;
    }

    public static List<List<Integer>> createLayers(List<Integer> digits) {
        int imageTotalPixels = IMAGE_WIDTH * IMAGE_HEIGHT;
        List<List<Integer>> layers = new ArrayList<>();
        List<Integer> layer = new ArrayList<>();
        for (int i = 0; i < digits.size(); i++) {
            if (i % imageTotalPixels == 0) {
                layer = new ArrayList<>();
                layers.add(layer);
            }
            int digit = digits.get(i);
            layer.add(digit);
        }
        return layers;
    }

    public static List<Integer> readInput() {
        Scanner in = new Scanner(System.in);
        String digitLine = in.nextLine();
        List<Integer> inputDigits = new ArrayList<>();
        for (int i = 0; i < digitLine.length(); i++)
            inputDigits.add(Character.getNumericValue(digitLine.charAt(i)));
        return inputDigits;
    }
}
