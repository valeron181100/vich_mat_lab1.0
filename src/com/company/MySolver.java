package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MySolver {
    private static double[][] matrix;
    private static double[] freeMemberVector;
    private static List<Double> inaccuracies = new ArrayList<>();
    private static boolean isFromFile = false;
    private static boolean isRandom = false;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        if(args.length == 0){
            System.out.println("Укажите аргументы коммандной строки. Чтобы показать справку передайте ключ -i");
            System.exit(0);
        }else
        if(args[0].equals("-i")){
            System.out.println("Ключ -m позволит вам вручную ввести матрицу.");
            System.out.println("Ключ -f позволит вам автоматически прочитать матрицу из файла.");
            System.out.println("Ключ -r позволит вам автоматически создать случайную матрицу матрицу.");
            System.exit(0);
        }else
        if(args[0].equals("-m")){
            isFromFile = false;
        }else
        if(args[0].equals("-f")){
            isFromFile = true;
            scanner = new Scanner(new File("matrix.txt"));
        }else
        if(args[0].equals("-r")){
            isRandom = true;
        }else {
            System.out.println("Неизвестный ключ.");
            System.exit(0);
        }


        //scanner.useLocale(new Locale("Russian"));
        if(!isFromFile){
            System.out.println("Введите размер матрицы:");
        }
        int size = 0;
        if(isRandom || !isFromFile) {
            boolean isSizeInCorrect = true;
            while (isSizeInCorrect) {
                String line = scanner.nextLine();
                if (line.trim().matches("\\d?\\d")) {
                    size = Integer.parseInt(line);
                    if (size <= 20 && size > 0) {
                        isSizeInCorrect = false;
                    } else
                        System.out.println("Размерность была введена неверно. Введите размерность от 0 до 20.");
                } else
                    System.out.println("Размерность была введена неверно. Введите размерность от 0 до 20.");
            }
        }else
        size = scanner.nextInt();

        matrix = new double[size][size];
        freeMemberVector = new double[size];

    if(!isFromFile && !isRandom) {
        System.out.println("Введите матрицу. Пример матрицы, размерность которой равна 3:");
        System.out.println("2 2 10 14\n" +
                "10 1 1 12\n" +
                "2 10 1 13");
        System.out.println("---------------------------------------------------------------");
    }
    if(!isRandom)
        readInputMatrix(scanner, size);
    else{
        System.out.println("Сгенерированная матрица:");

        matrix = getRandomMatrix(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                double random = round(Math.random() * (100 + 50) - 50, 4);
                if(j == size){
                    freeMemberVector[i] = random;
                }
                System.out.print(random + "\t");
            }
            System.out.println();
        }
    }

    if(isFromFile)
        System.out.println("Матрица была считана с файла.");

        if(!isFromFile)
            System.out.println("Введите точность:");
        int iterCtr = 0;
        double eps;
        eps = scanner.nextDouble();

        if(eps <= 0){
            System.out.println("Неверная точность. Точность не может быть меньше либо равна нуля.");
            System.exit(0);
        }
        double[] prevVals = new double[size];
        double[] curVals = new double[size];
        for (int i = 0; i < size; i++) {
            prevVals[i] = 0.0;
            curVals[i] = 0.0;
        }


        if(tryDiagonallyDominant(matrix)){

            matrix = transformMatrix(matrix);

            //Задаем начальное приближение
            for (int i = 0; i < size; i++) {
                prevVals[i] = freeMemberVector[i];
            }
            for (;;) {

                for (int i = 0; i < matrix.length; i++) {
                    double sum = 0;
                    for (int j = 0; j < matrix.length; j++) {
                        sum += matrix[i][j] * prevVals[j];
                    }
                    curVals[i] = round(sum + freeMemberVector[i], 5);
                }

                iterCtr++;

                if(isIterationsFinish(eps, prevVals, curVals)) break;
                prevVals = curVals.clone();

            }
            System.out.println("Ответ:");
            System.out.print("[");
            for (int i = 0; i < curVals.length; i++) {
                System.out.print(curVals[i] + "\t");
            }
            System.out.print("]\n");

            System.out.print("Столбец погрешностей:\n[");
            for (int i = 0; i < inaccuracies.size(); i++) {
                System.out.print(inaccuracies.get(i) + "\t");
            }
            System.out.print("]\n");

            System.out.println("Количество итераций: " + iterCtr);
            
        }else{
            System.out.println("Невозможно достичь диагонального преобладания!");
        }


    }

    static boolean tryDiagonallyDominant(double[][] matrix){

        //getting indices of max elements in each row

        int[] maxIndices = new int[matrix.length];

        for(int i = 0; i < matrix.length; i++){
            double max = 0;
            int maxIndex = 0;
            for (int j = 0; j < matrix.length; j++) {
                if(Math.abs(matrix[i][j]) > max){
                    max = Math.abs(matrix[i][j]);
                    maxIndex = j;
                }
            }
            maxIndices[i] = maxIndex;
        }

        //checking whether it is real to do Diagonally Dominant

        for (int i = 0; i < maxIndices.length; i++) {
            for (int j = i + 1 ; j < maxIndices.length; j++) {
                if (maxIndices[i] == maxIndices[j]){
                    return false;
                }
            }
        }

        //checking diagonally dominant

        for(int i = 0; i < matrix.length; i++){
            double diagElement = Math.abs(matrix[i][maxIndices[i]]);
            double sum = 0;
            for (int j = 0; j < matrix.length; j++) {
                if(j != maxIndices[i]){
                    sum += Math.abs(matrix[i][j]);
                }
            }
            if(diagElement <= sum)
                return false;
        }

        //transform matrix
        double[][] nMatrix = new double[matrix.length][matrix.length];
        double[] nFreeVector = new double[freeMemberVector.length];
        for (int i = 0; i < maxIndices.length; i++) {
            nFreeVector[maxIndices[i]] = freeMemberVector[i];
            for (int j = 0; j < matrix.length; j++) {
                nMatrix[maxIndices[i]][j] = matrix[i][j];
            }
        }
        MySolver.freeMemberVector = nFreeVector.clone();
        MySolver.matrix = nMatrix.clone();

        return true;
    }

    static double[][] transformMatrix(double[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            double diagElem = matrix[i][i];
            double b = MySolver.freeMemberVector[i];
            MySolver.freeMemberVector[i] = b / diagElem;
            for (int j = 0; j < matrix.length; j++) {
                if(i == j)
                    matrix[i][j] = 0;
                else
                    matrix[i][j] = -matrix[i][j]/diagElem;
            }
        }
        return matrix;
    }

    static boolean isIterationsFinish(double eps, double[] prevVals, double[] curVals){
        double maxInaccuracie = 0.0;
        for (int i = 0; i < prevVals.length; i++) {
            if(Math.abs(curVals[i] - prevVals[i]) > maxInaccuracie)
                maxInaccuracie = Math.abs(curVals[i] - prevVals[i]);
            if(Math.abs(curVals[i] - prevVals[i]) >= eps){
                inaccuracies.add(maxInaccuracie);
                return false;
            }
        }
        inaccuracies.add(maxInaccuracie);
        return true;
    }

    static void readInputMatrix(Scanner scanner, int size){

        int i = 0;
        while (i < size) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
           // lineScanner.useLocale(new Locale("Russian"));
            if(line.matches("(-?\\d+([\\\\.|,][0-9]+)?(\\s+)?)+")){
                List<Double> inputValues = new ArrayList<>();
                while(lineScanner.hasNextDouble())
                    inputValues.add(lineScanner.nextDouble());
                if(inputValues.size() > size + 1){
                    System.out.println("Введено больше значений, чем ожидалось.\nПопробуйте еще раз:");
                    continue;
                }
                if(inputValues.size() < size + 1){
                    System.out.println("Введено меньше значений, чем ожидалось.\nПопробуйте еще раз:");
                    continue;
                }

                for (int j = 0; j < inputValues.size(); j++) {
                    if(j == inputValues.size() - 1){
                        freeMemberVector[i] = inputValues.get(j);
                    }else
                        matrix[i][j] = inputValues.get(j);
                }

                int k = 0;
                i++;
            }else{
                System.out.println("В строке были найдены неверные значения.\nПопробуйте еще раз:");
                continue;
            }

        }
    }

    static double[][] getRandomMatrix(int size){
        double[][] randomMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    randomMatrix[i][j] = -50 + (50 + 50) * Math.random();
            }
        }

        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += Math.abs(randomMatrix[i][j]);
            }
            randomMatrix[i][i] = sum + 1 + ((sum + 10) - (sum + 1)) * Math.random();
        }
        return randomMatrix;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
