package com.tnc.study.tennisstore.domain.product;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ProductPriceException extends ApiException {
    @Serial
    private static final long serialVersionUID = -2651815416209572274L;

    public ProductPriceException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
