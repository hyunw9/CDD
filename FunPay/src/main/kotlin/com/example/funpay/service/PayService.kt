package com.example.funpay.service

import com.example.funpay.controller.PayRequest
import com.example.funpay.controller.PayRequestParams
import com.example.funpay.controller.PayResponse
import com.example.funpay.entity.BankTxStatus.BANK_SUCCESS
import com.example.funpay.entity.Payment
import com.example.funpay.entity.PaymentStatus.FAILED
import com.example.funpay.entity.PaymentStatus.SUCCESS
import com.example.funpay.repository.PaymentRepository
import com.example.funpay.service.PayResult.PAY_FAIL
import com.example.funpay.service.PayResult.PAY_SUCCESS
import com.example.funpay.service.adapter.BankAdapter
import com.example.funpay.service.adapter.BankAdapter.BankResponse
import com.example.funpay.service.adapter.DiscountAdapter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PayService(
    private val paymentRepository: PaymentRepository,
    private val bankAdapter: BankAdapter,
    private val discountAdapter: DiscountAdapter
) {
    @Transactional
    fun processPay(payRequestParams: PayRequestParams): PayResponse {

        // 포인트 결제(30%)
        val pointResponse: MethodResponse = payPoint(payRequestParams.pointAmount)

        //상품권 결제(20%)
        val voucherResponse = if (pointResponse.payResult == PAY_SUCCESS) {
            payVoucher(payRequestParams.voucherAmount)
        } else pointResponse

        val payResult = if (voucherResponse.payResult == PAY_SUCCESS) {
            payBank(payRequestParams.bankAmount).status == BANK_SUCCESS
        } else false

        // 결제 생성 및 저장
        val payment = savePayment(payRequestParams, payResult)

        return PayResponse(
            amount = payment.amount,
            status = payment.status
        )
    }

    private fun payBank(
        bankAmount: Int
    ): BankResponse {
        val discountRemainRate = 1 - discountAdapter.getDiscountRate(bankAmount)
        val discountedBankRequest = (bankAmount * discountRemainRate).toInt()
        return bankAdapter.payByBank(discountedBankRequest)
    }

    private fun savePayment(
        payRequest: PayRequestParams,
        payResult: Boolean
    ): Payment {
        val payment = Payment(
            amount = payRequest.bankAmount,
            productName = payRequest.productName,
            merchantName = payRequest.merchantName,
            status = if (payResult) SUCCESS else FAILED
        )
        return paymentRepository.save(payment)
    }

    private fun payPoint(pointAmount: Int): MethodResponse {

        //할인 적용 (10%)
        val discountRemainRate = 1 - discountAdapter.getDiscountRate(pointAmount)
        val discountPointAmount = (pointAmount * discountRemainRate).toInt()

        val result = when {
            discountPointAmount > 100_000 -> PAY_FAIL
            else -> PAY_SUCCESS
        }

        return MethodResponse(discountPointAmount, result)
    }

    private fun payVoucher(voucherAmount: Int): MethodResponse {

        //할인 적용 (10%)
        val discountRemainRate = 1 - discountAdapter.getDiscountRate(voucherAmount)
        val discountPointAmount = (voucherAmount * discountRemainRate).toInt()

        val result = when {
            discountPointAmount > 100_000 -> PAY_FAIL
            else -> PAY_SUCCESS
        }

        return MethodResponse(discountPointAmount, result)
    }

    data class MethodResponse(
        val amount: Int,
        val payResult: PayResult
    )
}
//문제가 발생한 부분
//1. payPoint 함수에서 원본 payRequest.amount를 변경하고 있음
//2. payVoucher 함수는 이미 수정된 금액에서 20%를 계산
//3. 결과적으로 전체 금액이 아닌 남은 금액의 20%가 계산된다.

// 이런 문제가 발생하기 쉬운 이유 ?
// 1. 부수효과로 인한 예측 어려움
// - 함수가 자기 영역 밖의 상태를 변경
// - 함수 호출 순서에 따라 결과가 달라질 수 있음.
// - 디버깅과 테스트가 어려움

// 문제를 파악하기 어려운 이유
// 1. 코드의 흐름이 명시적이지 않다.
// - 상태 변경이 함수 내부에 숨어있다.
// - 함수의 반환값만으로는 부수효과를 알 수 없다.
// 2. 테스트의 어려움
// - 부수효과로 인해 독립적인 단위 테스트 작성이 어렵다 .
// - 통합 테스트에서도 문제를 발견하기 쉽지 않다.
