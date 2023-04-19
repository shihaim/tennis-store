package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class RequiredOrderLineException extends ApiException {
    @Serial
    private static final long serialVersionUID = 3101323942828370772L;

    public RequiredOrderLineException() {
        super(HttpStatus.BAD_REQUEST, "주문 상품은 최소 한 개 이상이어야 합니다.");
    }
}
