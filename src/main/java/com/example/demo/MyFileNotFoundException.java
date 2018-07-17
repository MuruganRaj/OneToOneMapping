package com.example.demo;

public class MyFileNotFoundException  extends RuntimeException{
    public MyFileNotFoundException(String message){
        super(message);
    }

    public MyFileNotFoundException(String message,Throwable thr){
        super(message,thr);
    }
}
