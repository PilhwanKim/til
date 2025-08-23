package dev.leonkim.kafkalecture.stream

import dev.leonkim.kafkalecture.model.OrderCountComparisonStats
import dev.leonkim.kafkalecture.model.PeriodStats
import dev.leonkim.kafkalecture.model.WindowedOrderCount
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyWindowStore
import org.slf4j.LoggerFactory
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.log

@Service
class OrderStreamsService(
    private val factory: StreamsBuilderFactoryBean
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun orderCountComparison() : OrderCountComparisonStats? {
        return try {
            val stream = factory.kafkaStreams
            if (stream == null ||  stream.state() != KafkaStreams.State.RUNNING) {
                return null
            }

            val store : ReadOnlyWindowStore<String, WindowedOrderCount> = stream.store(
                StoreQueryParameters.fromNameAndType("order-count-store", QueryableStoreTypes.windowStore())
            )

            val now = Instant.now()

            /*
                   9시
                   8시 55분 ~ 9시 까지의 데이터
                   8시 50분 ~ 8시 55분 까지의 데이터
             */

            val currentPeriodEnd = now
            val currentPeriodStart = now.minusSeconds(300) // 5분전

            val prevPeriodEnd = currentPeriodStart
            val prevPeriodStart = currentPeriodStart.minusSeconds(300)

            val currentCount = countForPeriod(store, currentPeriodStart, currentPeriodEnd) //  8시 55분 ~ 9시 까지의 데이터
            val previousCount = countForPeriod(store, prevPeriodStart, prevPeriodEnd) // 8시 50분 ~ 8시 55분 까지의 데이터

            val changeCount = currentCount - previousCount
            val changePercentage = if (previousCount > 0) {
                (changeCount.toDouble() / previousCount.toDouble()) * 100.0
            } else if (currentCount > 0) {
                100.0
            } else {
                0.0
            }

            OrderCountComparisonStats(
                currentPeriod = PeriodStats(
                    windowStart = LocalDateTime.ofInstant(currentPeriodStart, ZoneOffset.UTC),
                    windowEnd = LocalDateTime.ofInstant(currentPeriodEnd, ZoneOffset.UTC),
                    orderCount = currentCount
                ),
                previousPeriod = PeriodStats(
                    windowStart = LocalDateTime.ofInstant(prevPeriodStart, ZoneOffset.UTC),
                    windowEnd = LocalDateTime.ofInstant(prevPeriodEnd, ZoneOffset.UTC),
                    orderCount = previousCount
                ),
                changeCount = changeCount,
                changePercentage = changePercentage,
                isIncreasing = changeCount > 0
            )
        } catch (e : Exception) {
            logger.error("Failed to get streams info", e.message)
            return null
        }
    }

    private fun countForPeriod(
        store: ReadOnlyWindowStore<String, WindowedOrderCount>,
        startTime: Instant,
        endTime: Instant,
    ): Long {
        var totalCount = 0L

        store.fetchAll(startTime, endTime).use { iter ->
            while (iter.hasNext()) {
                val entry = iter.next()
                totalCount += entry.value.count
            }
        }

        return totalCount
    }

}