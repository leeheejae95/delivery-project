package org.delivery.common.api

import org.delivery.common.error.ErrorCode
import org.delivery.common.error.ErrorCodeIfs

data class Result (
    val resultCode: Int?=null,
    val resultMessage: String?=null,
    val resultDescription:String?=null
) {
    companion object { // companion object -> java에 static과 동일

        @JvmStatic
        fun OK(): Result {
            return Result(
                resultCode = ErrorCode.OK.getErrorCode(),
                resultMessage = ErrorCode.OK.getDescription(),
                resultDescription = "성공"
            )
        }

        @JvmStatic
        fun ERROR(errorCodeIfs: ErrorCodeIfs): Result {
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getDescription(),
                resultDescription = "에러발생"
            )
        }

        @JvmStatic
        fun ERROR(
            errorCodeIfs: ErrorCodeIfs,
            throwable: Throwable
        ): Result {
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getDescription(),
                resultDescription = throwable.localizedMessage
            )
        }

        @JvmStatic
        fun ERROR(
            errorCodeIfs: ErrorCodeIfs,
            description: String
        ): Result {
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getDescription(),
                resultDescription = description
            )
        }
    }

    
}