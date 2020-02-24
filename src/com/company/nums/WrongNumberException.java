package com.company.nums;

public class WrongNumberException extends NumberFormatException {

    private String number;
    private String result;
    private String message;
    public WrongNumberException(){
        super();
    }
    public WrongNumberException(String message){
        super(message);
    }

    public WrongNumberException(String val, String result, String message){
        super(message + ": " + val + " = " + result);
        this.number = val;
        this.result = result;
    }
}
