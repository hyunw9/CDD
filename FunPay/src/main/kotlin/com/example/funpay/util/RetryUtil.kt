package com.example.funpay.util

object RetryUtil {

    fun <T> retry(
        maxAttempts: Int = 3,
        delayMillis: Long = 500,
        currentAttempt: Int = 1,
        block: () -> T // block이 제네릭이기 때문에, 어떤 타입이던 상관없이 처리할 수 있다.
    ): T {
        return try {
            block()
        } catch (e: Exception) {
            if (currentAttempt >= maxAttempts) throw e
            Thread.sleep(delayMillis)
            retry(currentAttempt + 1, block = block) // 재귀를 통해서 구현, 기본 값이 있을 때, 값을 넣어줘야 한다.
        }
    }
}
//API가 동작하지 않는다면? 재시도 로직이 필요하다.
