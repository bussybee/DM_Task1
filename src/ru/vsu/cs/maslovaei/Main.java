package ru.vsu.cs.maslovaei;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        int[][] A = readIntArray2FromFile("input.txt");
        int detA = countMatrixDet(A, A.length);
        writeDetToFile("output.txt", detA);
    }

    public static int[] toPrimitive(Integer[] arr) {
        if (arr == null) {
            return null;
        }
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // автоматическая распаковка из объекта
            result[i] = arr[i];
        }
        return result;
    }

    public static int[] toIntArray(String str) {
        Scanner scanner = new Scanner(str);
        scanner.useLocale(Locale.ROOT);
        scanner.useDelimiter("(\\s|[,;])+");
        List<Integer> list = new ArrayList<>();
        while (scanner.hasNext()) {
            list.add(scanner.nextInt());
        }

        // из List<Integer> можно получить Integer[]
        Integer[] arr = list.toArray(new Integer[0]);
        // Integer[] -> int[]
        return Main.toPrimitive(arr);
    }

    /**
     * Конвертация массива строк в двухмерный массив чисел int[][]
     */
    public static int[][] toIntArray2(String[] lines) {
        int[][] arr2 = new int[lines.length][];
        for (int r = 0; r < lines.length; r++) {
            arr2[r] = toIntArray(lines[r]);
        }
        return arr2;
    }

    /**
     * Чтение всего текстового файла в массив строк
     */
    public static String[] readLinesFromFile(String fileName) throws FileNotFoundException {
        List<String> lines;
        try (Scanner scanner = new Scanner(new File(fileName), "UTF-8")) {
            lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            // обязательно, чтобы закрыть открытый файл
        }
        return lines.toArray(new String[0]);
    }

    /**
     * Чтение двухмерного массива из текстового файла
     */
    public static int[][] readIntArray2FromFile(String fileName) {
        try {
            return toIntArray2(readLinesFromFile(fileName));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static int countMatrixDet(int[][] A, int n) {
        int detA = 0;
        int degree = 1;

        if (n == 1) {
            return A[0][0];
        } else if (n == 2) {
            return A[0][0] * A[1][1] - A[0][1] * A[1][0];
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int[][] B = getMinor(A, i, j);
                    detA += degree * A[i][j] * countMatrixDet(B, n - 1);
                    degree = -degree;
                }
            }
        }
        return detA;
    }

    //нахождение минора - удаление i-ой строки и j-ого столбца
    public static int[][] getMinor(int[][] array, int row, int col) {
        int n = array.length;
        int offsetRow = n - 1; //Смещение индекса строки в матрице
        int offsetCol = array[0].length - 1; //Смещение индекса столбца в матрице
        int[][] result = new int[offsetRow][offsetCol];

        for (int i = 0; i < n; i++) {
            boolean isRowDeleted = row < i;
            int resultRowIndex = isRowDeleted ? i - 1 : i;

            for (int j = 0; j < array[i].length; j++) {
                boolean isColDeleted = col < j;
                int resultColIndex = isColDeleted ? j - 1 : 1;

                if (row != i && col != j) {
                    result[resultRowIndex][resultColIndex] = array[i][j];
                }
            }
        }
        return result;
    }

    public static void writeDetToFile(String fileName, int detA)
            throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(detA);
        }
    }
}
