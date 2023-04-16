package com.tnc.study.tennisstore.framework.web.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiResponse {
    private final String code;
    private final ApiResponseType type;
    private final String message;

    public static final ApiResponse OK = new ApiResponse(ApiResponseCode.OK);
    public static final ApiResponse INVALID_MESSAGE = new ApiResponse(ApiResponseCode.INVALID_MESSAGE);
    public static final ApiResponse FAILED_MESSAGE_PARSING = new ApiResponse(ApiResponseCode.FAILED_MESSAGE_PARSING);
    public static final ApiResponse BAD_REQUEST = new ApiResponse(ApiResponseCode.BAD_REQUEST);
    public static final ApiResponse NOT_FOUND = new ApiResponse(ApiResponseCode.NOT_FOUND);
    public static final ApiResponse METHOD_NOT_ALLOWED = new ApiResponse(ApiResponseCode.METHOD_NOT_ALLOWED);
    public static final ApiResponse UNAUTHORIZED = new ApiResponse(ApiResponseCode.UNAUTHORIZED);
    public static final ApiResponse FORBIDDEN = new ApiResponse(ApiResponseCode.FORBIDDEN);
    public static final ApiResponse INTERNAL_ERROR = new ApiResponse(ApiResponseCode.INTERNAL_ERROR);

    public ApiResponse(ApiResponseCode code) {
        this.code = code.getCode();
        this.type = code.getType();
        this.message = code.getMessage();
    }
    public ApiResponse(ApiResponseCode code, String message) {
        this.code = code.getCode();
        this.type = code.getType();
        this.message = message;
    }
    public ApiResponse(String code, ApiResponseType type, String
            message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }
}
