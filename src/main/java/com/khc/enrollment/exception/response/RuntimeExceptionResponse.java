package com.khc.enrollment.exception.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RuntimeExceptionResponse {
    private final String exceptionType = "runtime";
    private String exceptionName;
    private String message;
}