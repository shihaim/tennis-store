package com.tnc.study.tennisstore.framework.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class ApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3147625130350966937L;

    private HttpStatus httpStatus;

    public ApiException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApiException(HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase());
        this.httpStatus = httpStatus;
    }

    public ApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String message) {
        super(message);
    }
}
