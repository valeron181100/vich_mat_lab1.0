package com.company;

import com.company.nums.Rational;

import javax.script.ScriptException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws ScriptException, IOException {
        FileWriter writer = new FileWriter("matrix.txt");
        writer.write(2);
        writer.close();
    }
}
