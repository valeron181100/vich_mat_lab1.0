package com;

import com.company.NewtonInterpolator;
import com.sun.org.apache.xpath.internal.Arg;
import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestChords {

    public static final int MAX_ITERS = 1000;

    public static void main(String[] args) throws Exception {
        Function f = new Function("f(x) = x^3 - 3*x^2 - 8 * abs(x)");
        List<Double> points = new ArrayList<>();
        for (int i = 0; i <= 18; i += 2) {
            points.add((double)i);
        }
        double x = 3;

        NewtonInterpolator interpolator = new NewtonInterpolator(f, points, x);
        System.out.println(interpolator.interpolate());
    }

    private static double doMethodChord (Function f, double a, double b, double eps) {
        if(a > b) {
            double temp = a;
            a = b;
            b = temp;
        }
        double xCurr = a;
        if(a == 0){
            xCurr = (b - a) / 2;
        }else if ((b - a) / 2 == 0){
            xCurr = a;
        }
        double xPrev = 0;
        double xDoublePrev = 0;
        while(Math.abs(xCurr - xPrev) > eps){
            xDoublePrev = xPrev;
            xPrev = xCurr;
            double numerator = f.calculate(xPrev) * (xPrev - xDoublePrev);
            double denominator =(f.calculate(xPrev) - f.calculate(xDoublePrev));
            if(denominator == 0 || Double.isNaN(denominator)){
                throw new RuntimeException("Ошибка: метод не сходится.");
            }
            xCurr -= numerator/denominator;
        }
        return xCurr;
    }

    public static double doMethodTouch(Function f, double a, double b, double eps){
        Expression derF = new Expression("der(f(x), x , g)", f);
        Argument arg = new Argument("g = 0");
        derF.addArguments(arg);
        if(a > b) {
            double temp = a;
            a = b;
            b = temp;
        }
        double xCurr = a;
        if(a == 0){
            xCurr = (b - a) / 2;
        }else if ((b - a) / 2 == 0){
            xCurr = a;
        }

        double xPrev = 0;
        while(Math.abs(xCurr - xPrev) > eps){
            xPrev = xCurr;
            arg.setArgumentValue(xPrev);
            xCurr = xPrev - 0.1 * f.calculate(xPrev)/derF.calculate();
        }
        return xCurr;
    }
}
