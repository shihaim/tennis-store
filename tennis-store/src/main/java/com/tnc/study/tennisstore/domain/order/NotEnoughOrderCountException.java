package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotEnoughOrderCountException extends ApiException {
    @Serial
    private static final long serialVersionUID = -3111377306172054887L;

    public NotEnoughOrderCountException(String productName) {
        super(HttpStatus.BAD_REQUEST, "주문 수량은 최소 1개 이상 이어야 합니다. 상품명: " + productName);
    }
}
