package org.example.section3.code1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 작업1 실행")
    }
    launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 작업2 실행")
    }
    launch(Dispatchers.IO) {
        println("[${Thread.currentThread().name}] 작업3 실행")
    }
}