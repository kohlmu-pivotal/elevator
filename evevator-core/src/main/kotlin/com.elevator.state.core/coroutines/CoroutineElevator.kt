package com.elevator.state.core.coroutines

import com.elevator.state.Event
import com.elevator.state.StateProcessContext
import com.elevator.state.core.CommandResult
import com.elevator.state.core.Elevator
import com.elevator.state.core.ElevatorCommand
import com.elevator.state.core.ElevatorEvents
import io.vavr.control.Either
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlin.coroutines.CoroutineContext

class CoroutineElevator(elevatorIdentifier: String) : Elevator(elevatorIdentifier), CoroutineScope {

    private val resultChannel = Channel<CommandResult>(Channel.RENDEZVOUS)
    private val job = Job()

    private val commandProcessActor = actor<ElevatorCommand>(coroutineContext) {
        for (elevatorCommand in channel) {
            launch(coroutineContext) {
                val eventResult =
                    stateMachine.processEvent(elevatorCommand.event, elevatorCommand.stateProcessContext)
                resultChannel.send(CommandResult(eventResult))
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + newFixedThreadPoolContext(5, "RootCoroutineContext")

    fun close() {
        commandProcessActor.close()
        job.cancel()
    }

    suspend fun processCoroutineEvent(
        event: ElevatorEvents,
        stateProcessContext: StateProcessContext
    ): StateProcessContext = submitElevatorCommandForProcessing(ElevatorCommand(Event(event), stateProcessContext))

    private suspend fun submitElevatorCommandForProcessing(elevatorCommand: ElevatorCommand): StateProcessContext {
        commandProcessActor.send(elevatorCommand)
        return processCommandResult(resultChannel.receive())
    }


    private fun processCommandResult(commandResult: CommandResult): StateProcessContext {
        return when (val result = commandResult.result) {
            is Either.Left -> result.left
            is Either.Right -> fail(result.get().exception)
            else -> fail(IllegalArgumentException("There are only two options in an either"))
        }
    }
}

fun main() = runBlocking { ArbitraryCoroutineRunner(CoroutineElevator("Elevator")).runElevators() }
