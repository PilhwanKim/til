package org.example.section3.code1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val dedicatedDispatcher = Dispatchers.IO.limitedParallelism(2)
    repeat(100) {
        launch(dedicatedDispatcher) {
            println("[${Thread.currentThread().name}] 중요 작업 실행")
        }
    }
}