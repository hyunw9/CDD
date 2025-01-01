package com.example.funpay.service.adapter

import org.springframework.stereotype.Component

@Component
class DiscountAdapter {

    fun getDiscountRateV2(): (Int) -> Double = { amount ->
        when (amount) {
            in 100..1000 -> 0.2
            in 1001..10000 -> 0.1
            else -> 0.0
        }
        //함수를 리턴하는 함수
    }
}
