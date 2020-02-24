package com.company;

import com.company.nums.Rational;

import javax.script.ScriptException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws ScriptException {
        Scanner scanner = new Scanner(System.in);
        String line;
        do{
            line = scanner.nextLine().trim();
            Rational num = Rational.toRational(line);
            System.out.println(num.getNum() + "/" + num.getDiv());
        }while (!line.equals("q"));
    }
}
