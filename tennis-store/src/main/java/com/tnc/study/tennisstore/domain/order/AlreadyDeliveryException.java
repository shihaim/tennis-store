package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class AlreadyDeliveryException extends ApiException {
    @Serial
    private static final long serialVersionUID = -8295683350260904509L;

    public AlreadyDeliveryException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
