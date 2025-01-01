package com.example.funpay.util

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RetryUtilTest : StringSpec({

    "재시도 후 성공" {
        var attempts = 0

        val result = RetryUtil.retry {
            attempts++
            if(attempts<2) throw RuntimeException("2보다 작으면 에러")
            else "성공"
        }

        attempts shouldBe 2
        result shouldBe "성공"
    }

    "최대시도 횟수만큼 실패하면 예외" {
        var attempts = 1
        shouldThrow<RuntimeException> {
            RetryUtil.retry {
                attempts+=1
                throw RuntimeException("계속 실패")
            }
        }
    }


})
