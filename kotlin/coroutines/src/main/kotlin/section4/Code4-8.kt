package org.example.section4

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis()
    val lazyJob = launch(start = CoroutineStart.LAZY) {
        println("[${getElapsedTime(startTime)}] launch 코루틴 지연 실행")
    }
    delay(3000L) // 3000밀리초간 대기
    lazyJob.start() // 코루틴 실행 lazyJob.join()을 호출해도 실행됨
}