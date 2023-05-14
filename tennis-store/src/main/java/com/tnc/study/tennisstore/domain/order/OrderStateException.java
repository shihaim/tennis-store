package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class OrderStateException extends ApiException {
    @Serial
    private static final long serialVersionUID = -4810194851524788973L;

    public OrderStateException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
