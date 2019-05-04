package com.elevator.state.core.coroutines

import com.elevator.state.core.ElevatorEvents
import kotlinx.coroutines.*

class ArbitraryCoroutineRunner(private val elevator: CoroutineElevator) {
    fun runElevators() {
        val mainThreadContext = newFixedThreadPoolContext(24, "MainThreadContext")

        val instances = measureTime("Instance creation") {
            (0..1000000).map { index ->
                elevator.createNewInstance("E$index")
            }
        }

        val turnOnResults = measureTime("Turn On") {
            instances.map { instance ->
                //                    println("submitting thread ${Thread.currentThread().name}")
                val stateProcessContext = elevator.processCoroutineEvent(ElevatorEvents.TURN_ON, instance)
                    elevator.processCoroutineEvent(ElevatorEvents.FLOOR_SELECTED, stateProcessContext)
            }
        }
    }
}

fun <T> measureTime(description: String, block: suspend () -> T): T =
    runBlocking {
        val start = System.currentTimeMillis()
        val result = block.invoke()
        println("$description - Took ${System.currentTimeMillis() - start}ms to run")
        result
    }
