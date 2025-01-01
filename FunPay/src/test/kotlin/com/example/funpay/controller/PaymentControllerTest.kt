package com.example.funpay.controller

import com.example.funpay.entity.PaymentStatus
import com.example.funpay.exception.ErrorCode.INVALID_INPUT
import com.example.funpay.service.PayService
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(PaymentController::class)
class PaymentControllerTest @Autowired constructor(
  @MockkBean
  private var payService: PayService,
  private val mockMvc: MockMvc
) : BehaviorSpec({


  Given("유효한 결제 요청이 주어졌을 때") {
    val requestPayload = """
            {
                "amount": 1000,
                "productName": "상품1",
                "merchantName": "가맹점1"
            }
        """
    val expectedResponse = PayResponse(
      amount = 1000,
      status = PaymentStatus.SUCCESS
    )

    every { payService.processPay(any()) } returns expectedResponse

    When("결제 요청을 처리하면") {
      val result = mockMvc.post("/v1/payment") {
        contentType = MediaType.APPLICATION_JSON
        content = requestPayload
      }

      Then("응답이 성공 상태여야 하고 데이터가 정확해야 한다") {
        result.andExpect {
          status { isOk() }
          jsonPath("$.amount") { value(expectedResponse.amount) }
          jsonPath("$.status") { value(expectedResponse.status.name) }
        }
      }
    }
  }

  Given("유효하지 않은 요청이 주어졌을 때") {
    val invalidRequestPayload = """
            {
                "amount": -100,
                "productName": "",
                "merchantName": ""
            }
        """

    When("결제 요청을 처리하면") {
      val result = mockMvc.post("/v1/payment") {
        contentType = MediaType.APPLICATION_JSON
        content = invalidRequestPayload
      }.andExpect {
        status { isOk() }
      }

      Then("응답은 INVALID_INPUT") {
        result.andExpect {
          jsonPath("$.errorCode") { value(INVALID_INPUT.name) }
        }
      }
    }
  }
}) {
  override fun extensions() = listOf(SpringExtension)
}
