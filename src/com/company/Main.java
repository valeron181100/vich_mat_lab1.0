package com.company;

import com.company.nums.Rational;

import javax.script.ScriptException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ScriptException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter size:\n");
        int size = Integer.parseInt(scanner.next());
        double eps;
        Rational[][] matrix = new Rational[size][size + 1];
        System.out.println("Enter matrix:\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                matrix[i][j] = Rational.toRational(scanner.next());
            }
        }
        System.out.println("Enter ratio:\n");
        eps = scanner.nextDouble();

        System.out.println("Input data:\n");

        System.out.println("Ratio equals " + eps + ";\n");
        System.out.println("Input matrix: \n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                if(j == size){
                    System.out.print("|\t" + matrix[i][j] + "\t");
                }else{
                    System.out.print(matrix[i][j] + "\t");
                }
            }
            System.out.println("\n");
        }
    }
}
