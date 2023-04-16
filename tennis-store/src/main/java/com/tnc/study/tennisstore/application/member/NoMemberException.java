package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NoMemberException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1142096933385250068L;

    public NoMemberException() {
        super(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다.");
    }
}
