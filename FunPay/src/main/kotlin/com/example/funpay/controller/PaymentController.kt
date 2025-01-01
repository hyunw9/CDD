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
) {
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
    val productName: String,
    val merchantName: String,
    val pointAmount: Int,
    val voucherAmount: Int,
    val bankAmount: Int,
) {
    fun toDiscountedRequestParams(
      discountRateFunc: (Int) -> Double,
      //왜 결과값을 바로 받지 않는가? (Rate 만 받으면 되지 않는가? )
      //-> 정책 자체가 유동적인 경우를 상정한다.
      //-> 정책이 변경되더라도 코드 수정이 필요 없도록 하기 위함.
    ) =
        this.copy(
            pointAmount = (pointAmount * (1- discountRateFunc(pointAmount))).toInt(),
            voucherAmount = (voucherAmount* (1-discountRateFunc(voucherAmount))).toInt(),
            bankAmount = (bankAmount * (1-discountRateFunc(bankAmount))).toInt()
        )

    //개선된 점
    //1. 일관된 정책 적용
    //- 하나의 할인 함수를 모든 계산에 사용
    //- 정책 변경 시 한 곳만 수정
    //2. 참조 투명성 향상
    //- 할인 계산 로직이 예측 가능해짐
    //- 테스트가 용이해짐
    //3. 함수형 프로그래밍 활용
    //- 고차 함수를 통한 유연한 정책 사용
    //- 부수효과 제거
    //Adapter 에도 함수로 정책을 받아서 사용하도록 변경
}

data class PayResponse(var amount: Int, var status: PaymentStatus)
