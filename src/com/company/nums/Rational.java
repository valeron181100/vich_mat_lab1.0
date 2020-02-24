package com.company.nums;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Rational extends Number implements Comparable<Number>{
    private int num;
    private int div;

    public Rational(int num, int div){
        if(div == 0) throw new NumberFormatException("Divider can't be zero!");
        this.num = num;
        this.div = div;
    }

    public Rational(Rational num, int div){
        if(div == 0) throw new NumberFormatException("Divider can't be zero!");
        this.num = num.getNum();
        this.div = div * num.getDiv();
    }
    public Rational(int num, Rational div){
        if(div.getNum() == 0) throw new NumberFormatException("Divider can't be zero!");
        this.num = num * div.getDiv();
        this.div = div.getNum();
    }

    public Rational(Rational num, Rational div){
        if(num.getDiv() * div.getNum() == 0) throw new NumberFormatException("Divisior can't be zero!");
        this.num = num.getNum() * div.getDiv();
        this.div = num.getDiv() * div.getNum();
    }



    public static Rational toRational(Double val){
        for (int i = 1;; i++) {
            double tem = val / (1D / i);
            if (Math.abs(tem - Math.round(tem)) < 0.01)
                return new Rational((int)Math.round(tem), i);
        }
    }

    public static Rational toRational(Float val){
        return toRational(val.doubleValue());
    }

    public static Rational toRational(String val) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String res = engine.eval(val).toString();
        if(res.equals("Infinity"))
            throw  new WrongNumberException(val, res, "Divider can't be zero!");
        return Rational.toRational(Double.parseDouble(res));
    }

    public double toDouble(){
        return getNum()/getDiv();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getDiv() {
        return div;
    }

    public void setDiv(int div) {
        this.div = div;
    }

    @Override
    public int compareTo(Number o) {
        return 0;
    }

    @Override
    public int intValue() {
        return Double.valueOf(toDouble()).intValue();
    }

    @Override
    public long longValue() {
        return Double.valueOf(toDouble()).longValue();
    }

    @Override
    public float floatValue() {
        return Double.valueOf(toDouble()).floatValue();
    }

    @Override
    public double doubleValue() {
        return toDouble();
    }
}
