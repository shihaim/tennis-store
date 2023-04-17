package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NoProductException extends ApiException {
    @Serial
    private static final long serialVersionUID = -5069014479490973525L;

    public NoProductException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
