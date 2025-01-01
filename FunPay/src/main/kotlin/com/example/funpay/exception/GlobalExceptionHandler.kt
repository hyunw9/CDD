package com.example.funpay.exception

import com.example.funpay.exception.ErrorCode.INVALID_INPUT
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

class FunPayException(
  val errorCode: ErrorCode,
  message: String = errorCode.message
) : RuntimeException(message)

enum class ErrorCode(val message: String) {
  INVALID_INPUT("입력 데이터가 유효하지 않습니다.")
}

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(FunPayException::class)
  fun handleFunPayException(
    ex: FunPayException
  ): ErrorResponse =
    ErrorResponse(ex.errorCode, ex.message ?: "Internal Server Error")

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(
    ex: MethodArgumentNotValidException
  ): ErrorResponse =
    ErrorResponse(INVALID_INPUT, ex.message ?: INVALID_INPUT.message)
}

data class ErrorResponse(
  val errorCode: ErrorCode,
  val message: String,
)
        