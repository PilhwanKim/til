package org.example

import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit>{
    println("[${Thread.currentThread().name}] 실행")
}