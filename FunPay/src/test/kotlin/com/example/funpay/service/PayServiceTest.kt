//package com.example.funpay.service
//
//import com.example.funpay.entity.BankTxStatus.AMOUNT_BELOW_MINIMUM
//import com.example.funpay.entity.BankTxStatus.LACK_BALANCE
//import com.example.funpay.entity.PaymentStatus.FAILED
//import com.example.funpay.entity.PaymentStatus.SUCCESS
//import com.example.funpay.repository.PaymentRepository
//import com.example.funpay.service.adapter.BankAdapter
//import com.example.funpay.testutil.TestDataFactory.dummyBankResponse
//import com.example.funpay.testutil.TestDataFactory.dummyPayRequest
//import com.example.funpay.testutil.TestDataFactory.dummyPayment
//import io.kotest.core.spec.style.BehaviorSpec
//import io.kotest.matchers.shouldBe
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//
//class PayServiceTest : BehaviorSpec({
//  val paymentRepository = mockk<PaymentRepository>()
//  val bankAdapter = mockk<BankAdapter>()
//  val payService = PayService(paymentRepository, bankAdapter)
//
//  Given("유효한 결제 요청이 주어졌을 때") {
//    every {
//      bankAdapter.payByBank(100)
//    } returns dummyBankResponse()
//    every { paymentRepository.save(any()) } returns dummyPayment()
//
//    When("결제를 처리하면") {
//      val response = payService.processPay(dummyPayRequest())
//
//      Then("결제가 성공적으로 완료된다") {
//        response.amount shouldBe 100
//        response.status shouldBe SUCCESS
//      }
//
//      Then("결제 기록 저장된다") {
//        verify { paymentRepository.save(match { it.status == SUCCESS }) }
//      }
//    }
//  }
//
//  Given("유효하지 않은 결제 요청이 주어졌을 때") {
//    When("잔액 부족으로 실패한 경우") {
//      every { bankAdapter.payByBank(200) } returns
//          dummyBankResponse(status = LACK_BALANCE)
//      every { paymentRepository.save(any()) } returns dummyPayment(status = FAILED)
//
//      Then("결제가 실패 상태로 저장된다") {
//        val response = payService.processPay(dummyPayRequest(amount = 200))
//
//        response.status shouldBe FAILED
//        verify { paymentRepository.save(match { it.status == FAILED }) }
//      }
//    }
//
//    When("은행 요청이 AMOUNT_BELOW_MINIMUM 상태로 반환되면") {
//      every { bankAdapter.payByBank(0) } returns
//          dummyBankResponse(status = AMOUNT_BELOW_MINIMUM)
//      every { paymentRepository.save(any()) } returns dummyPayment(status = FAILED)
//
//      Then("결제가 실패 상태로 저장된다") {
//        val response = payService.processPay(dummyPayRequest(amount = 0))
//
//        response.status shouldBe FAILED
//        verify { paymentRepository.save(match { it.status == FAILED }) }
//      }
//    }
//  }
//})
