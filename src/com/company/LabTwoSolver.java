package com.company;

import com.company.nums.IntegrateIterKind;
import static java.lang.Double.isFinite;
import java.util.Scanner;

public class LabTwoSolver {

    private static double a = 0;
    private static double b = 1;
    private static double x = 1;
    private static double eps = 0.01;
    private static final double CHECK_BREAK_NUM = 0.00001;

    private static int n = 2;
    private static double curS = 0.0;
    private static double prevS = 0.0;
    private static double h = 0;
    private static double accuracy = 10;
    private static int FOO_NUM = 0;
    private static int integrateIterKind = 2;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Список функций:");
        System.out.println("0. e^(1/x) / x^2");
        System.out.println("1. (x^2 * sin(x)) / 10");
        System.out.println("2. 1/sqrt(4 - x^2)");
        System.out.println("3. arctan(1/x)");
        System.out.println("Введите номер функции, интеграл которой вы хотите найти:");
        FOO_NUM = scanner.nextInt();
        if(FOO_NUM > 4 || FOO_NUM < 0){
            System.out.println("Неверный номер команды.");
            System.exit(0);
        }
        System.out.println("Введите границу a и b:");
        a = scanner.nextDouble();
        b = scanner.nextDouble();
        trySwapBounds();
        System.out.println("Введите точность:");
        eps = scanner.nextDouble();

        if(eps <= 0){
            System.out.println("Неверная точность. Точность не может быть меньше либо равна нуля.");
            System.exit(0);
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Метод левых прямоугольников:");
        doRectanglesIntegrate(IntegrateIterKind.LEFT);
        System.out.println("-----------------------------------------------");
        System.out.println("Метод средних прямоугольников:");
        doRectanglesIntegrate(IntegrateIterKind.MIDDLE);
        System.out.println("-----------------------------------------------");
        System.out.println("Метод правых прямоугольников:");
        doRectanglesIntegrate(IntegrateIterKind.RIGHT);

    }

    public static void doRectanglesIntegrate(IntegrateIterKind iterKind){
        doIter(a, b, eps, iterKind);
        while(accuracy > eps){
            doIter(a, b, eps, iterKind);
            accuracy = (1.0/3.0)*Math.abs(curS - prevS);
        }
        System.out.println("Ответ: " + curS);
        System.out.println("Количество разбиений: " + n);
        System.out.println("Погрешность: " + accuracy);
    }

    public static void doIter(double a, double b, double eps, IntegrateIterKind iterKind){
        x = a;
        prevS = curS;
        curS = 0.0;
        h = (b - a) / n;
        double k = 0;
        switch (iterKind){
            case RIGHT: k = h; break;
            case MIDDLE: k = h/2; break;
        }
        int i = 0;
        while (i < n) {

            switch (iterKind){
                case LEFT:
                    if(i > 0) k = h;
                    x += k;
                    if(!isIntegralCorrect(x, i, IntegrateIterKind.LEFT)){
                        System.out.println("Интеграл не может быть посчитан.");
                        System.out.println("Количество разбиений: " + n);
                        System.exit(0);
                    }
                    break;
                case RIGHT:
                    x += k;
                    if(!isIntegralCorrect(x, i, IntegrateIterKind.RIGHT)){
                        System.out.println("Интеграл не может быть посчитан.");
                        System.out.println("Количество разбиений: " + n);
                        System.exit(0);
                    }
                    break;
                case MIDDLE:
                    if(i > 0) k = h;
                    x += k;

                    if(!isIntegralCorrect(x, i, IntegrateIterKind.MIDDLE)){
                        System.out.println("Интеграл не может быть посчитан.");
                        System.out.println("Количество разбиений: " + n);
                        System.exit(0);
                    }
                    break;
            }

            curS += foo(x, FOO_NUM) * h;
            i++;
        }
        n *= 2;
    }

    public static double foo(double x, int fooNum){
        switch(fooNum){
            // 1 to 2
            case 0: return Math.pow(Math.E, 1/x)/(x*x);
            // 4 to 9
            case 1: return (x*x*Math.sin(x)) / 10;
            // -2 to 2
            case 2: return 1/Math.sqrt(4 - x*x);
            // -2 to 4
            case 3: return  Math.atan(1/x);

            default: return 0;
        }
    }

    public static boolean isIntegralCorrect(double x, int iterNum, IntegrateIterKind iterKind){
        double preResult = 0;
        switch (iterKind){
            case LEFT:
                if(iterNum == 0){
                    double f = foo(x + CHECK_BREAK_NUM, FOO_NUM);
                    preResult = isFinite(f) ? f : 0;
                }
                else{
                    double f = (foo(x + CHECK_BREAK_NUM, FOO_NUM) + foo(x - CHECK_BREAK_NUM, FOO_NUM))/2;
                    preResult = isFinite(f) ? f : 0;
                }
                break;
            case RIGHT:
                if(iterNum == n - 1){
                    double f = foo(x - CHECK_BREAK_NUM, FOO_NUM);
                    preResult = isFinite(f) ? f : 0;
                }else{
                    double f = (foo(x + CHECK_BREAK_NUM, FOO_NUM) + foo(x - CHECK_BREAK_NUM, FOO_NUM))/2;
                    preResult = isFinite(f) ? f : 0;
                }
                break;
            case MIDDLE:
                double f = (foo(x + CHECK_BREAK_NUM, FOO_NUM) + foo(x - CHECK_BREAK_NUM, FOO_NUM))/2;
                preResult = isFinite(f) ? f : 0;
                break;
        }
        if(preResult == 0) return false;
        else{
            return isFinite(preResult);
        }
    }

    public static void trySwapBounds(){
        if(a > b) {
            double temp = a;
            a = b;
            b = temp;
            System.out.println("Так как a > b, они были поменяны местами.");
        }
    }

}
