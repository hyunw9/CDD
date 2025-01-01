package com.example.funpay.testutil

import com.example.funpay.controller.PayRequest
import com.example.funpay.entity.BankTxStatus
import com.example.funpay.entity.Payment
import com.example.funpay.entity.PaymentStatus
import com.example.funpay.service.adapter.BankAdapter

object TestDataFactory {

  fun dummyPayRequest(
    amount: Int = 100,
    productName: String = "Default Product",
    merchantName: String = "Default Merchant",
  ) = PayRequest(
    amount = amount,
    productName = productName,
    merchantName = merchantName,
  )

  fun dummyPayment(
    id: Long = 1L,
    amount: Int = 100,
    productName: String = "Default Product",
    merchantName: String = "Default Merchant",
    status: PaymentStatus = PaymentStatus.SUCCESS
  ) = Payment(
    id = id,
    amount = amount,
    productName = productName,
    merchantName = merchantName,
    status = status
  )

  fun dummyBankResponse(
    amount: Int = 100,
    status: BankTxStatus = BankTxStatus.BANK_SUCCESS
  ) = BankAdapter.BankResponse(
    amount = amount,
    status = status
  )
}
