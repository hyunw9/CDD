package com.example.funpay.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CacheUtilTest : StringSpec({

    "캐시 데이터 확인" {
        val cache = CacheUtil<Int,String>(timeOutMillis = 100L)
        var computingCount = 0

        val loader = {
            computingCount++
            "result"
        }

        cache.getOrLoad(1, loader) shouldBe "result"
        computingCount shouldBe 1

        cache.getOrLoad(1, loader) shouldBe "result"
        computingCount shouldBe 1

        cache.getOrLoad(1004, loader) shouldBe "result"
        computingCount shouldBe 2

        cache.getOrLoad(1009, loader) shouldBe "result"
        computingCount shouldBe 3

        Thread.sleep(120L)


    }

})
