package dev.leonkim.kafkalecture.basic

import dev.leonkim.kafkalecture.model.OrderEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component


@Component
class OrderEventConsumer(
) {
    private val logger = LoggerFactory.getLogger(OrderEventConsumer::class.java)

    @KafkaListener(
        topics = ["\${kafka.topics.orders}"],
        groupId = "order-processing-group",
        concurrency = "3",
        containerFactory = "orderEventKafkaListenerContainerFactory",
    )
    fun processOrder(
        @Payload orderEvent: OrderEvent,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: String,
        @Header(KafkaHeaders.OFFSET) offset: Long,
    ) {
        logger.info("Received order event: {}", orderEvent)
        try {
            processLogic()
            logger.info("Received order event: {}", orderEvent)
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
        }
    }

    private fun processLogic() {
        Thread.sleep(100)
    }
}

@Component
class OrderAnalyticsConsumer {

    private val logger = LoggerFactory.getLogger(OrderAnalyticsConsumer::class.java)

    @KafkaListener(
        topics = ["\${kafka.topics.orders}"],
        groupId = "order-analytics-group",
        concurrency = "2",
        containerFactory = "orderEventKafkaListenerContainerFactory"
    )
    fun collectAnalytics(
        @Payload orderEvent: OrderEvent,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int
    ) {
        logger.info("Collecting analytics for order {} from partition {}",
            orderEvent.orderId, partition)

        try {
            updateCustomerStatistics(orderEvent)

        } catch (ex: Exception) {
            logger.error("Failed to collect analytics for order {}: {}",
                orderEvent.orderId, ex.message)
        }
    }

    private fun updateCustomerStatistics(orderEvent: OrderEvent) {
        logger.debug("Updated customer statistics for {}", orderEvent.customerId)
    }

}

@Component
class OrderNotificationConsumer {

    private val logger = LoggerFactory.getLogger(OrderNotificationConsumer::class.java)

    @KafkaListener(
        topics = ["\${kafka.topics.orders}"],
        groupId = "order-notification-group",
        concurrency = "1",
        containerFactory = "orderEventKafkaListenerContainerFactory"
    )
    fun sendNotifications(
        @Payload orderEvent: OrderEvent,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int
    ) {
        logger.info("Sending notifications for order {} from partition {}",
            orderEvent.orderId, partition)

        try {
            if (isHighValueOrder(orderEvent)) {
                sendHighValueOrderSms(orderEvent)
            }

        } catch (ex: Exception) {
            logger.error("Failed to send notifications for order {}: {}",
                orderEvent.orderId, ex.message)
        }
    }

    private fun sendHighValueOrderSms(orderEvent: OrderEvent) {
        logger.info("SMS sent for high-value order {}", orderEvent.orderId)
    }

    private fun isHighValueOrder(orderEvent: OrderEvent): Boolean {
        return orderEvent.price.compareTo(java.math.BigDecimal("1000")) >= 0
    }
}