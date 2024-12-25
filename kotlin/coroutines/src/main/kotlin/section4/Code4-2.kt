package org.example.section4

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val updateTokenJob = launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 토큰 업데이트 시작")
        delay(100L)
        println("[${Thread.currentThread().name}] 토큰 업데이트 완료")
    }

    val networkCallJob = launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 네트워크 요청")
    }
}