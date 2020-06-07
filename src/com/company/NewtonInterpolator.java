package com.company;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.List;

public class NewtonInterpolator {

    private Function function;
    private List<Double> X;
    private double xVal;

    public NewtonInterpolator(Function function, List<Double> X, double xValue) throws Exception {
        this.function = function;
        if(!function.checkSyntax()){
            throw new Exception("Ошибка: Неверный синтакс введённой функции!");
        }
        this.xVal = xValue;
        this.X = X;
    }

    public double interpolate(){

        List<Double> Y = new ArrayList<>();
        for (Double p : X) {
            Y.add(function.calculate(p));
        }

        return getValue(Y, this.X, this.xVal);
    }

    private double getValue(List<Double> Y, List<Double> X, double xValue){
        double result = Y.get(0);
        double buff;
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        for (int i = 1; i < Y.size(); i++) {
            xList.clear();
            yList.clear();
            buff = 1;

            for (int j = 0; j <= i; j++) {
                xList.add(X.get(j));
                yList.add(Y.get(j));
                if(j < i)
                    buff *= xValue - X.get(j);
            }

            result += getDifference(yList, xList) * buff;
        }
        return result;
    }

    private double getDifference(List<Double> Y, List<Double> X){
        if(Y.size() > 2){
            List<Double> yLeft = new ArrayList<>(Y);
            List<Double> xLeft = new ArrayList<>(X);
            yLeft.remove(0);
            xLeft.remove(0);

            List<Double> yRight = new ArrayList<>(Y);
            List<Double> xRight = new ArrayList<>(X);
            yRight.remove(Y.size() - 1);
            xRight.remove(X.size() - 1);

            return (getDifference(yLeft, xLeft) - getDifference(yRight, xRight))/(X.get(X.size() - 1) - X.get(0));
        }else if(Y.size() == 2){
            return (Y.get(1)- Y.get(0))/(X.get(1) - X.get(0));
        }else{
            throw new RuntimeException("Not available parameter!");
        }
    }
}
