package org.delivery.api.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.delivery.common.api.Api;
import org.delivery.common.error.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 예상치 못한 예외가 일어나거나 캐치하지 못한 에러가 일어났을때 처리해줌
 */
@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE) // 순서지정 -> 가장 마지막에 실행적용
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception(Exception exception) { // 모든에러가 여기에 옴
        log.error("", exception);

        return ResponseEntity
                .status(500)
                .body(Api.ERROR(ErrorCode.SERVER_ERROR));
    }
}
