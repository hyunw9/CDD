package com.example.funpay.repository

import com.example.funpay.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Long>
