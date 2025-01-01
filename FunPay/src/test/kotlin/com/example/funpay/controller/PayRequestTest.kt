package com.example.funpay.controller

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PayRequestTest : StringSpec({
    "포인트 30%, 상품권 20%, 은행 50%" {
        val result = PayRequest(10000, "productName","merchantName")
            .toPayRequestParams()

        assertSoftly(result){
            it.pointAmount shouldBe 3000
            it.voucherAmount shouldBe 2000
            it.bankAmount shouldBe 5000
        }
    }


})
