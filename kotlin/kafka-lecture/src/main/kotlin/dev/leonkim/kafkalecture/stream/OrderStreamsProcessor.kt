package dev.leonkim.kafkalecture.stream

import dev.leonkim.kafkalecture.model.*
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.WindowStore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.kafka.support.serializer.JsonSerde
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Duration


@Component
class OrderStreamsProcessor(
    @Value("\${kafka.topics.orders}") private val ordersTopic: String,
    @Value("\${kafka.topics.high-value-orders}") private val highValueOrdersTopic: String,
    @Value("\${kafka.topics.fraud-alerts}") private val fraudAlertsTopic: String,
) {
    private val logger = LoggerFactory.getLogger(OrderStreamsProcessor::class.java)

    private val orderEventSerde = createJsonSerde<OrderEvent>()
    private val fraudAlertSerde = createJsonSerde<FraudAlert>()
    private val windowedOrderCountSerde = createJsonSerde<WindowedOrderCount>()
    private val windowedSalesDataSerde = createJsonSerde<WindowedSalesData>()

    private inline fun <reified T> createJsonSerde(): JsonSerde<T> {
        return JsonSerde<T>().apply {
            configure(mapOf(
                "spring.json.trusted.packages" to "dev.leonkim.kafkalecture.model",
                "spring.json.add.type.headers" to false,
                "spring.json.value.default.type" to T::class.java.name
            ), false)
        }
    }

    @Bean
    fun orderProcessingTopology(builder: StreamsBuilder): Topology {
        val orderStream: KStream<String, OrderEvent> = builder.stream(ordersTopic, Consumed.with(Serdes.String(), orderEventSerde))

        highValueStream(orderStream)
        fraudStream(orderStream)
        orderCountStatsStream(orderStream)
        salesStatsStream(orderStream)

        return builder.build()
    }

    private fun highValueStream(orderStream :  KStream<String, OrderEvent>) {
        val highValueStream = orderStream.filter { _, orderEvent ->
            logger.info("Filtering high Value Stream order: {}", orderEvent.orderId)
            orderEvent.price >= BigDecimal("1000")
        }

        highValueStream.to(highValueOrdersTopic, Produced.with(Serdes.String(), orderEventSerde))
    }

    private fun fraudStream(orderStream :  KStream<String, OrderEvent>) {
        val fraudStream = orderStream.filter { _, orderEvent ->
            orderEvent.price >= BigDecimal("5000") ||
                    orderEvent.quantity > 100 ||
                    orderEvent.price.multiply(BigDecimal.valueOf(orderEvent.quantity.toLong())) >= BigDecimal("10000")
        }.mapValues { orderEvent ->
            val reason = when {
                orderEvent.price >= BigDecimal("5000") -> "High single order value"
                orderEvent.quantity > 100 -> "High quantity order"
                else -> "High total order value"
            }

            val severity = when {
                orderEvent.price >= BigDecimal("10000") -> FraudSeverity.CRITICAL
                orderEvent.price >= BigDecimal("5000") -> FraudSeverity.HIGH
                orderEvent.quantity > 100 -> FraudSeverity.MEDIUM
                else -> FraudSeverity.LOW
            }

            FraudAlert(
                orderId = orderEvent.orderId,
                customerId = orderEvent.customerId,
                reason = reason,
                severity = severity,
            )
        }

        fraudStream.to(fraudAlertsTopic, Produced.with(Serdes.String(), fraudAlertSerde))
    }

    private fun orderCountStatsStream(orderStream: KStream<String, OrderEvent>) {
        orderStream
            .groupByKey(Grouped.with(Serdes.String(), orderEventSerde))
            .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
            .aggregate(
                { WindowedOrderCount() },
                { _, _, aggregate -> aggregate.increment() },
                Materialized.`as`<String, WindowedOrderCount, WindowStore<Bytes, ByteArray>>("order-count-store")
                    .withValueSerde(windowedOrderCountSerde)
            )
    }

    private fun salesStatsStream(orderStream: KStream<String, OrderEvent>) {
        /*
            <"customer1", OrderEvent(orderId="order1", customerId="customer1", price=100)>
            <"customer2", OrderEvent(orderId="order2", customerId="customer2", price=200)>
            <"customer1", OrderEvent(orderId="order3", customerId="customer1", price=150)>


            customer1: [OrderEvent(order1, 100), OrderEvent(order3, 150)]
            customer2: [OrderEvent(order2, 200)]
        */
        orderStream
            .groupBy(
                { key, orderEvent -> orderEvent.customerId },
                Grouped.with(Serdes.String(), orderEventSerde)
            )
            .windowedBy(TimeWindows.of(Duration.ofHours(1)))
            .aggregate(
                { WindowedSalesData() },
                { _, orderEvent, aggregate -> aggregate.add(orderEvent.price) },
                Materialized.`as`<String, WindowedSalesData, WindowStore<Bytes, ByteArray>>("sales-stats-store")
                    .withValueSerde(windowedSalesDataSerde)
            )
    }
}