package com.project.ecommarcemodernapp.exception;

import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionStatus status;

    public ApplicationException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.status = exceptionStatus;
    }

    public static void throwIf(boolean condition, ExceptionStatus exceptionStatus) {
        if (condition) {
            throw new ApplicationException(exceptionStatus);
        }
    }
}
