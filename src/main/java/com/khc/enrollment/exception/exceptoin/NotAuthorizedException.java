package com.khc.enrollment.exception.exceptoin;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(String string){
        super(string);
    }
    public NotAuthorizedException(){
        super("권한이 없습니다");
    }
}
