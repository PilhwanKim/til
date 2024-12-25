package org.example.section4

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis()
    val lazyJob = launch(start = CoroutineStart.LAZY) {
        println("[${getElapsedTime(startTime)}] launch 코루틴 지연 실행")
    }
}