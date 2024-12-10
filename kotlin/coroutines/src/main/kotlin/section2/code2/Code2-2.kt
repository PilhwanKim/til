package org.example.section2.code2

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking<Unit>(CoroutineName("runBlocking 코루틴")) {
    println("[${Thread.currentThread().name}] runBlocking 코루틴 실행")

    launch(CoroutineName("launch 코루틴")) {
        println("[${Thread.currentThread().name}] launch 코루틴 실행")
    }
} 