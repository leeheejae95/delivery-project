/*
package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter // Getter를 달아주면 오버라이드 안해줘도됨
public enum ErrorCode implements ErrorCodeIfs {

    OK(200,200,"성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된요청"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버에러"),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "null point"),
;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
*/
