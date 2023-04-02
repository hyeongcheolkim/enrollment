package com.khc.enrollment.entity;

public enum MajorType {
    ORIGIN("본전공"),
    SUB("부전공"),
    MULTI("복수전공");

    private String korean;

    MajorType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
