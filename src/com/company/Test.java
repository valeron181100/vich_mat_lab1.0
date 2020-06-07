package com.company;

import com.company.nums.Rational;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Function;

import javax.script.ScriptException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws ScriptException, IOException {
        Function foo = new Function("f(x,y) = x^2 + y");
        foo.setArgumentValue(0, 2);
        foo.setArgumentValue(1, 3);
        System.out.println(foo.calculate());
    }
}
