package com.tnc.study.tennisstore.framework.web.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.tnc.study.tennisstore.framework.web.response.ApiResponseType.*;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {
    OK(SUCCESS, "정상적으로 처리되었습니다."),
    INVALID_MESSAGE(ERROR, "유효하지 않은 요청입니다."),
    FAILED_MESSAGE_PARSING(ERROR, "HTTP 메시지 파싱에 실패 하였습니다."),
    BAD_REQUEST(ERROR, "Bad Request"),
    NOT_FOUND(ERROR,"Not Found"),
    METHOD_NOT_ALLOWED(ERROR, "Method Not Allowed"),
    UNAUTHORIZED(ERROR, "Unauthorized"),
    FORBIDDEN(ERROR, "Forbidden"),
    INTERNAL_ERROR(ERROR, "Internal error");

    private final ApiResponseType type;
    private final String message;

    public String getCode() {
        return this.name();
    }
}
