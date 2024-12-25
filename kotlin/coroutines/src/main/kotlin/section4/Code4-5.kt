package org.example.section4

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val convertImageJob1 = launch(Dispatchers.IO) {
        delay(1000L)
        println("[${Thread.currentThread().name}] 이미지1 변환 완료")
    }

    val convertImageJob2 = launch(Dispatchers.IO) {
        delay(1000L)
        println("[${Thread.currentThread().name}] 이미지2 변환 완료")
    }

    joinAll(convertImageJob1, convertImageJob2)

    val uploadImageJob = launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 이미지1,2 업로드")
    }
}