package com.example.funpay.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import java.math.BigDecimal

@Entity
class Payment(
  @Id @GeneratedValue(strategy = IDENTITY)
  var id: Long? = null,
  val amount: Int,
  val productName: String,
  val merchantName: String,
  @Enumerated(EnumType.STRING)
  var status: PaymentStatus
)

enum class PaymentStatus {
  SUCCESS, FAILED
}

enum class BankTxStatus {
  BANK_SUCCESS, LACK_BALANCE, AMOUNT_BELOW_MINIMUM
}
        