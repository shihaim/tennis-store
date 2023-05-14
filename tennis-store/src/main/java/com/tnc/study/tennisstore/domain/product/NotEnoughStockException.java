package com.tnc.study.tennisstore.domain.product;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotEnoughStockException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1837572060743341955L;

    public NotEnoughStockException(String productName) {
        super(HttpStatus.BAD_REQUEST, "재고 수량이 부족합니다. 상품명: " + productName);
    }
}
