package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.framework.web.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class DuplicateMemberException extends ApiException {
    @Serial
    private static final long serialVersionUID = 6664739201022484650L;

    public DuplicateMemberException() {
//        super(message);
        super(HttpStatus.BAD_REQUEST, "Email 중복");
    }
}
