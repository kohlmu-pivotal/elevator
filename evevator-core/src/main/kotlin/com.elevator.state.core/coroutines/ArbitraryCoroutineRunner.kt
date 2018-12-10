package com.elevator.state.core.coroutines

import com.elevator.state.core.ElevatorEvents
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.streams.toList

class ArbitraryCoroutineRunner(private val elevator: CoroutineElevator) {
    suspend fun runElevators() {
        val instances = measureTime("Instance creation") {
            (0..1000000).map { index ->
                elevator.createNewInstance("E$index")
            }.toList()
        }

        val turnOnResults = measureTime("Turn On") {
            instances.parallelStream().map { instance ->
                GlobalScope.async {
                    elevator.processCoroutineEvent(ElevatorEvents.TURN_ON, instance)
                }
            }.toList()
        }

        val map = measureTime("await") {
            turnOnResults.map { it.await() }.toList()
        }

        measureTime("Floor Selected")
        {
            map.parallelStream().map { stateProcessContext ->
                GlobalScope.launch {
                    elevator.processCoroutineEvent(ElevatorEvents.FLOOR_SELECTED, stateProcessContext)
                }

            }.toList().joinAll()
        }
    }
}

suspend fun <T> measureTime(description: String, block: suspend () -> T): T {
    val start = System.currentTimeMillis()
    val result = block.invoke()
    println("$description - Took ${System.currentTimeMillis() - start}ms to run")
    return result
}