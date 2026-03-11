package com.project.ecommarcemodernapp.exception.enums;

import com.project.ecommarcemodernapp.exception.constant.ExceptionMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND_EXCEPTION),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.ORDER_NOT_FOUND),
    ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.ORDER_ITEM_NOT_FOUND),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_REQUEST),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, ExceptionMessage.EMAIL_ALREADY_EXISTS),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, ExceptionMessage.USERNAME_ALREADY_EXISTS),
    ORDER_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, ExceptionMessage.ORDER_CODE_ALREADY_EXISTS),
    INVALID_ORDER_ITEM_QUANTITY(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_ORDER_ITEM_QUANTITY),
    INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_PRODUCT_PRICE),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, ExceptionMessage.USER_CREDENTIALS_WRONG),;

    private final HttpStatus httpStatus;
    private final String message;
}
