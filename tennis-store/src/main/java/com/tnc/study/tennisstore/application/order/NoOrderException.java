package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NoOrderException extends ApiException {
    @Serial
    private static final long serialVersionUID = -8684417074252375270L;

    public NoOrderException() {
        super(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다.");
    }
}
