package com.elevator.state.core.coroutines

import com.elevator.state.Event
import com.elevator.state.StateProcessContext
import com.elevator.state.core.CommandResult
import com.elevator.state.core.Elevator
import com.elevator.state.core.ElevatorCommand
import com.elevator.state.core.ElevatorEvents
import io.vavr.control.Either
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlin.coroutines.CoroutineContext

class CoroutineElevator(elevatorIdentifier: String) : Elevator(elevatorIdentifier), CoroutineScope {

    private val job = Job()

    private suspend fun commandProcessProducer(elevatorCommand: ElevatorCommand) =
        CommandResult(stateMachine.processEvent(elevatorCommand.event, elevatorCommand.stateProcessContext))


    override val coroutineContext: CoroutineContext by lazy {
        job + newFixedThreadPoolContext(
            5,
            "RootCoroutineContext"
        )
    }

    fun close() {
        coroutineContext.cancelChildren()
        job.cancel()
    }


    fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

    suspend fun processCoroutineEvent(
        event: ElevatorEvents,
        stateProcessContext: StateProcessContext
    ): StateProcessContext {
        log("Running event ${event.name}")
        val result = submitElevatorCommandForProcessing(ElevatorCommand(Event(event), stateProcessContext))
        log("Completed Event ${event.name}")
        return result
    }

    private suspend fun submitElevatorCommandForProcessing(elevatorCommand: ElevatorCommand): StateProcessContext =
        processCommandResult(commandProcessProducer(elevatorCommand))


    private suspend fun processCommandResult(commandResult: CommandResult): StateProcessContext {
        return when (val result = commandResult.result) {
            is Either.Left -> result.left
            is Either.Right -> fail(result.get().exception)
            else -> fail(IllegalArgumentException("There are only two options in an either"))
        }
    }
}

fun main() = runBlocking { ArbitraryCoroutineRunner(CoroutineElevator("Elevator")).runElevators() }
