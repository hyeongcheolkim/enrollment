package com.khc.enrollment.exception.exceptoin;

public class DuplicatedEntityException extends RuntimeException{
    public DuplicatedEntityException() {
        super("Entity가 중복입니다");
    }

    public DuplicatedEntityException(String msg){
        super(msg);
    }
}
