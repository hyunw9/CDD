package com.example.funpay.controller

import com.example.funpay.entity.PaymentStatus
import com.example.funpay.service.PayService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class PaymentController(private val payService: PayService) {

  @PostMapping("/payment")
  fun createPayment(
    @RequestBody @Valid request: PayRequest
  ): PayResponse = payService.processPay(request.toPayRequestParams())
}

data class PayRequest(
  @field:Min(1) var amount: Int,
  @field:NotBlank var productName: String,
  @field:NotBlank var merchantName: String,
){
  fun toPayRequestParams() = PayRequestParams(
    amount,
    productName,
    merchantName,
    pointAmount = (amount * 0.3).toInt(),
    voucherAmount = (amount * 0.2).toInt(),
    bankAmount = (amount * 0.5).toInt()
  )
  //1. 순수성 확보
  //- 동일 입력에 대해 항상 동일한 결과
  //- 외부 상태 변경 X

  //2. 테스트 용이성
  //- 단위 테스트 작성 용이(모킹 등 기법 불필요)
  //- 결과 검증이 명확함

  //3. 코드 안전성
  //- 부수효과로 인한 버그 가능성 감소
  //- 코드의 의도가 명확해짐.
}

data class PayRequestParams(
  val originalAmount: Int,
  val productName : String,
  val merchantName: String,
  val pointAmount : Int,
  val voucherAmount : Int,
  val bankAmount : Int,
)

data class PayResponse(var amount: Int, var status: PaymentStatus)
