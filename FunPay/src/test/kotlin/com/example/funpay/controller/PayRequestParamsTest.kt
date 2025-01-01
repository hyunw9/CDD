package com.example.funpay.controller

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PayRequestParamsTest : StringSpec({

    "할인률 적용 테스트" {
        val discountRateFunc: (Int) -> Double = {
            when (it) {
                in 1000..3000 -> 0.1
                in 3001..100000 -> 0.2
                else -> 0.0
            }
        }

        val result = PayRequest(10000, "p", "m")
            .toPayRequestParams()
            .toDiscountedRequestParams(discountRateFunc) // 함수를 데이터 처럼 전달

        assertSoftly(result){
            it.pointAmount shouldBe 2700
            it.voucherAmount shouldBe 1800
            it.bankAmount shouldBe 4500
        }

    }


})
