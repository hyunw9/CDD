package com.example.funpay.service.adapter

import org.springframework.stereotype.Component

@Component
class DiscountAdapter {

    fun getDiscountRate(amount : Int) : Double{
        val discountRate:Double = when(amount){
            in 100..1000 -> 0.1
            in 1001 .. 10000 -> 0.2
            else -> 0.0
        }

        return discountRate
    }
}
