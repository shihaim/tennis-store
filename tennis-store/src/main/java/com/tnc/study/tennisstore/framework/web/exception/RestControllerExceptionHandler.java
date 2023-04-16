package com.tnc.study.tennisstore.framework.web.exception;

import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
import com.tnc.study.tennisstore.framework.web.response.ApiResponseCode;
import com.tnc.study.tennisstore.framework.web.response.ApiResponseDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    /**
     * api 요청을 받았을 때, requestBody Object의 Validation을 체크해서 통과하지 못하면 발생하는 Exception handling
     * 어떤 항목이 통과하지 못했는지 확인해서 해당 항목으로 응답 메세지 만들어서 반환
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDetail<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("error: {}, errorMessage: {}", e.getClass().getName(), e.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> invalidValueMap = new HashMap<>();

        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            Class<?> fieldType = bindingResult.getFieldType(fieldName);

            // fieldType이 없거나 enum이 아닐 때
            if (fieldType == null || !fieldType.isEnum()) {
                invalidValueMap.put(fieldName, message);
                continue;
            }

            // enum 일 때
            List<String> codeProperties = new ArrayList<>();
            Enum<?>[] enumConstants = (Enum<?>[]) fieldType.getEnumConstants();
            for (Enum<?> enumConstant : enumConstants) {
                String codeProperty = enumConstant.toString();
                codeProperties.add(codeProperty);
            }

            String availableCodes = StringUtils.collectionToDelimitedString(codeProperties, ", ");
            invalidValueMap.put(fieldName, "허용 값 [" + availableCodes + "]");
        }

        ApiResponseDetail<Map<String, String>> responseBody = new ApiResponseDetail<>(ApiResponseCode.INVALID_MESSAGE, invalidValueMap);

        return ResponseEntity.badRequest().body(responseBody);
    }

    /**
     * api 요청을 받았을 때, requestBody Object의 형식이 이상할 때 (특히 JSON 콤마 문제나 Integer 타입인데 String 타입으로 요청한 경우 등등)
     * 파싱이 안되서 발생한 Exception handling
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("error: {}, errorMessage: {}", e.getClass().getName(), e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.FAILED_MESSAGE_PARSING);
    }

    /**
     * api 요청을 받았을 때, url에 맵핑되는 Controller가 없을 때 발생한 Exception handling
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("error: {}, errorMessage: {}", e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.NOT_FOUND);
    }

    /**
     * api 요청을 받았을 때, url에 맵핑은 되는데 HTTP Method가 맵핑이 안될 때 발생한 Exception handling
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("error: {}, errorMessage: {}", e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResponse.METHOD_NOT_ALLOWED);
    }

    /**
     * ApiException
     * @param e
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException e) {
        log.error("error: {}, errorMessage: {}",e.getClass().getName(), e.getMessage());
        ApiResponse apiResponse = switch (e.getHttpStatus()) {
            case BAD_REQUEST -> new ApiResponse(ApiResponseCode.BAD_REQUEST, e.getMessage());
            case NOT_FOUND -> new ApiResponse(ApiResponseCode.NOT_FOUND, e.getMessage());
            case UNAUTHORIZED -> new ApiResponse(ApiResponseCode.UNAUTHORIZED, e.getMessage());
            default -> new ApiResponse(ApiResponseCode.INTERNAL_ERROR, e.getMessage());
        };

        return ResponseEntity.status(e.getHttpStatus()).body(apiResponse);
    }

    /**
     * Handler들에 잡히지 않은 Exception handling
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        log.error("error: {}, errorMessage: {}", e.getClass().getName(), e.getMessage());

        return ResponseEntity.internalServerError()
                .body(ApiResponse.INTERNAL_ERROR);
    }
}
