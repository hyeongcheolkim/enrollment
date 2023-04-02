package com.khc.enrollment.exception.exceptoin;

public class NoExistEntityException extends RuntimeException{
    public NoExistEntityException() {
        super("Entity를 찾을 수 없습니다");
    }
}
