package com.elevator.state.core.coroutines

import com.elevator.state.core.ElevatorEvents
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ArbitraryCoroutineRunner(private val elevator: CoroutineElevator) {
    suspend fun runElevators() {
        val mainThreadContext = newFixedThreadPoolContext(24, "MainThreadContext")

        val instances = measureTime(mainThreadContext, "Instance creation") {
            (0..1000000).map { index ->
                elevator.createNewInstance("E$index")
            }
        }

        val turnOnResults = measureTime(mainThreadContext, "Turn On") {
            instances.map { instance ->
                //                    println("submitting thread ${Thread.currentThread().name}")
                val stateProcessContext = elevator.processCoroutineEvent(ElevatorEvents.TURN_ON, instance)
                elevator.processCoroutineEvent(ElevatorEvents.FLOOR_SELECTED, stateProcessContext)
            }
        }
    }
}

suspend fun <T> measureTime(context: CoroutineContext, description: String, block: suspend () -> T): T {
    val start = System.currentTimeMillis()
    val result = block.invoke()
    println("$description - Took ${System.currentTimeMillis() - start}ms to run")
    return result
}

