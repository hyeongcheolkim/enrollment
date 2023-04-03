package com.khc.enrollment.exception.exceptoin;

public class NotAuthenticatedException extends RuntimeException{
    public NotAuthenticatedException(String string){
        super(string);
    }
    public NotAuthenticatedException(){
        super("권한이 없습니다");
    }
}
