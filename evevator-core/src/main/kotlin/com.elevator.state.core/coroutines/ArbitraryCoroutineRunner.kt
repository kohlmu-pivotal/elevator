package com.elevator.state.core.coroutines

import com.elevator.state.core.ElevatorEvents
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.newFixedThreadPoolContext

class ArbitraryCoroutineRunner(private val elevator: CoroutineElevator) {
    suspend fun runElevators() {
        val mainThreadContext = newFixedThreadPoolContext(5, "MainThreadContext")

        val instances = measureTime("Instance creation") {
            (0..1000000).map { index ->
                elevator.createNewInstance("E$index")
            }
        }

        val turnOnResults = measureTime("Turn On") {
            instances.map { instance ->
                GlobalScope.async(mainThreadContext) {
                    elevator.processCoroutineEvent(ElevatorEvents.TURN_ON, instance)
                }
            }
        }

        val awaitMap = measureTime("await") {
            turnOnResults.map { it.await() }
        }

        measureTime("Floor Selected")
        {
            awaitMap.map { stateProcessContext ->
                GlobalScope.async(mainThreadContext) {
                    elevator.processCoroutineEvent(ElevatorEvents.FLOOR_SELECTED, stateProcessContext)
                }

            }.joinAll()
        }
    }
}

suspend fun <T> measureTime(description: String, block: suspend () -> T): T {
    val start = System.currentTimeMillis()
    val result = block.invoke()
    println("$description - Took ${System.currentTimeMillis() - start}ms to run")
    return result
}