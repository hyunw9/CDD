package com.example.funpay.service.adapter

import com.example.funpay.entity.BankTxStatus
import org.springframework.stereotype.Component
import java.util.*

@Component
class BankAdapter {

  data class BankResponse(val amount: Int, val status: BankTxStatus)

  fun payByBank(amount: Int): BankResponse {
    val status = when {
      amount > 100_000 -> BankTxStatus.LACK_BALANCE
      amount < 100 -> BankTxStatus.AMOUNT_BELOW_MINIMUM
      else -> BankTxStatus.BANK_SUCCESS
    }
    return BankResponse(amount, status)
  }
}
