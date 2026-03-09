package com.project.ecommarcemodernapp.exception;

import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;

public class ApplicationException extends RuntimeException {

    public ApplicationException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
    }

    public static void throwIf(boolean condition, ExceptionStatus exceptionStatus) {
        if (condition) {
            throw new ApplicationException(exceptionStatus);
        }
    }
}
