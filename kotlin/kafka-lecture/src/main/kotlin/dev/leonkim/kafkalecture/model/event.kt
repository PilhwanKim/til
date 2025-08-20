package dev.leonkim.kafkalecture.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderEvent(
    val orderId: String,
    val customerId: String,
    val quantity: Int,
    val price: BigDecimal,
    val eventType: String = "ORDER_CREATED",
    val status: String = "PENDING",
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
)