package com.tnc.study.tennisstore.framework.web.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiResponseDetail<T> extends ApiResponse {

    private final T detail;

    public ApiResponseDetail(ApiResponseCode status, T detail) {
        super(status);
        this.detail = detail;
    }

    public ApiResponseDetail(ApiResponseCode status, String message, T detail) {
        super(status, message);
        this.detail = detail;
    }

    public ApiResponseDetail(String status, ApiResponseType type, String message, T detail) {
        super(status, type, message);
        this.detail = detail;
    }
}
