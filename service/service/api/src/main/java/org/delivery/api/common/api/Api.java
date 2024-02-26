/*
package org.delivery.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.error.ErrorCodeIfs;

import javax.validation.Valid;

//{
//    result: {
//            "result_code" : "200",
//            "result_message" : "OK",
//            "result_description" : "성공"
//        }
//    "body" : {
//
//        }
//
//}
*/
/**
 * { -> Api<T>부분
 *
 *     "body" : { -> private T body부분
 *     
 *     }
 * }
 *//*

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> {

    private Result result;

    @Valid
    private T body;

    public static <T> Api<T> OK(T data) {
        var api = new Api<T>();
        api.result = Result.OK();
        api.body = data;
        return api;
    }

    public static Api<Object> ERROR(Result result) {
        var api = new Api<Object>();
        api.result = result;
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs) {
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs);
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, Throwable tx) {
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, tx);
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, String description) {
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, description);
        return api;
    }
}
*/
