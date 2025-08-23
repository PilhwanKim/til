package dev.leonkim.kafkalecture.controller

import dev.leonkim.kafkalecture.model.OrderCountComparisonStats
import dev.leonkim.kafkalecture.model.StatsResponse
import dev.leonkim.kafkalecture.stream.OrderStreamsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stats")
class StatsController(
    private val orderStreamsService: OrderStreamsService
) {

    @GetMapping("/orders/count")
    fun getOrderCountStats(): ResponseEntity<StatsResponse<OrderCountComparisonStats>> {
        val stats = orderStreamsService.orderCountComparison()
        val response = StatsResponse(
            success = true,
            data = stats,
            message = "Order count statistics retrieved successfully"
        )
        return ResponseEntity.ok(response)
    }
}