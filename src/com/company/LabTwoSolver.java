package com.company;

import com.company.nums.IntegrateIterKind;
import static java.lang.Double.isFinite;
import java.util.Scanner;

public class LabTwoSolver {

    private static double a = 0;
    private static double b = 1;
    private static double x = 1;
    private static double eps = 0.01;
    private static final double CHECK_BREAK_NUM = 0.0001;

    /**
     * Количество разбиений.
     */
    private static int n = 2;
    private static double curS = 0.0;
    private static double prevS = 0.0;
    private static double h = 0;

    public static void main(String[] args) {
        System.out.println("Введите границу a и b:");
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextDouble();
        b = scanner.nextDouble();
        System.out.println("Введите точность:");
        eps = scanner.nextDouble();
        doRecanglesIntegrate(IntegrateIterKind.RIGHT);
        System.out.println("Ответ: " + curS);
        System.out.println("Количество разбиений: " + n);
    }

    public static void doRecanglesIntegrate(IntegrateIterKind iterKind){
        doIter(a, b, eps, iterKind);
        while((1.0/3.0)*Math.abs(curS - prevS) > eps){
            doIter(a, b, eps, iterKind);
        }
    }

    public static void doIter(double a, double b, double eps, IntegrateIterKind iterKind){
        prevS = curS;
        curS = 0.0;
        h = (b - a) / n;
        int i = 1;
        while (i < n) {

            switch (iterKind){
                case LEFT:
                    x = a + i*h - h;
                    if(!isIntegralCorrect(x, i, IntegrateIterKind.LEFT)){
                        System.out.println("Интеграл не может быть посчитан.");
                        System.out.println("Количество разбиений: " + n);
                        System.exit(0);
                    }
                    break;
                case RIGHT:
                    x = a + i*h;
                    if(!isIntegralCorrect(x, i, IntegrateIterKind.RIGHT)){
                        System.out.println("Интеграл не может быть посчитан.");
                        System.out.println("Количество разбиений: " + n);
                        System.exit(0);
                    }
                    break;
                case MIDDLE:
                    x = (a + i*h) - h/2;
                    if(!isIntegralCorrect(x, i, IntegrateIterKind.MIDDLE)){
                        System.out.println("Интеграл не может быть посчитан.");
                        System.out.println("Количество разбиений: " + n);
                        System.exit(0);
                    }
                    break;
            }

            curS += foo(x) * h;
            i++;
        }
        n *= 2;
    }


    public static double foo(double x){
        return secFoo(x);
    }

    public static boolean isIntegralCorrect(double x, int iterNum, IntegrateIterKind iterKind){
        double preResult = 0;
        switch (iterKind){
            case LEFT:
                if(iterNum == 0){
                    double f = foo(x + CHECK_BREAK_NUM);
                    preResult = isFinite(f) ? f : 0;
                }
                else{
                    double f = (foo(x + CHECK_BREAK_NUM) + foo(x - CHECK_BREAK_NUM))/2;
                    preResult = isFinite(f) ? f : 0;
                }
                break;
            case RIGHT:
                if(iterNum == n - 1){
                    double f = foo(x - CHECK_BREAK_NUM);
                    preResult = isFinite(f) ? f : 0;
                }else{
                    double f = (foo(x + CHECK_BREAK_NUM) + foo(x - CHECK_BREAK_NUM))/2;
                    preResult = isFinite(f) ? f : 0;
                }
                break;
            case MIDDLE:
                double f = (foo(x + CHECK_BREAK_NUM) + foo(x - CHECK_BREAK_NUM))/2;
                preResult = isFinite(f) ? f : 0;
                break;
        }
        if(preResult == 0) return false;
        else{
            return isFinite(preResult);
        }
    }

    public static double firstFoo(double x){
        return Math.pow(Math.E, 1/x)/(x*x);
    }

    public static double secFoo(double x){
        return (x*x*Math.sin(x)) / 10;
    }

    public static double thirdFoo(double x){
        return 1/Math.sqrt(4 - x*x);
    }
    public static double fourthFoo(double x){
        return Math.tan(x);
    }
}
