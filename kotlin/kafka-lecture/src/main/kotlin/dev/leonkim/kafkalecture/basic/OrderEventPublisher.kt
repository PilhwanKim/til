package dev.leonkim.kafkalecture.basic

import dev.leonkim.kafkalecture.model.OrderEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component


@Component
class OrderEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, OrderEvent>,
    @Value("\${kafka.topics.orders}") private val orderTopic: String,
) {
    private val logger = LoggerFactory.getLogger(OrderEventPublisher::class.java)

    fun publishOrderEvent(orderEvent: OrderEvent) {
        try {
            // hash(key) % 토픽의 파티션 수
            kafkaTemplate.send(orderTopic, orderEvent.orderId, orderEvent)
                .whenComplete { _, ex ->
                    if (ex != null) {
                        logger.error("Error when publishing order event", ex)
                    } else {
                        logger.info("Successfully published order event")
                    }
                }
        } catch (ex: Exception) {
            logger.error("Error Publishing order event", ex)
        }
    }
}