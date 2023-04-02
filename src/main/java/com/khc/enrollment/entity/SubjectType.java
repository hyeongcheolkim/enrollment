package com.khc.enrollment.entity;

public enum SubjectType {
    GENERAL_ELECTIVE("교양선택"),
    GENERAL_CORE("교양필수"),
    MAJOR_ELECTIVE("전공선택"),
    MAJOR_CORE("전공필수");

    private String korean;

    SubjectType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
