package com.elevator.state.core

import com.elevator.state.Event
import com.elevator.state.Failure
import com.elevator.state.StateMachine
import com.elevator.state.StateProcessContext
import com.elevator.state.builder.elevator
import io.vavr.control.Either
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

open class Elevator(private val elevatorIdentifier: String) {
    protected val stateMachine: StateMachine
    private val elevatorDoors: ElevatorDoors
    private val controlPanel: ControlPanel

    init {
        stateMachine = initializeElevatorStateMachine(this)
        elevatorDoors = ElevatorDoors(this)
        controlPanel = ControlPanel(this)
    }

    suspend fun createNewInstance(instanceName: String): StateProcessContext = stateMachine.initializeNewInstance(instanceName)

    private fun initializeElevatorStateMachine(elevator: Elevator): StateMachine =
        elevator {
            name = elevatorIdentifier
            initialState = ElevatorState.OFF.name
            state {
                name = ElevatorState.ON.name
                onEnter = { println("Initializing Elevator.....") }
                onExit = { println("Initialized....\nReady") }
            }
            state {
                name = ElevatorState.MOVING.name
                onEnter = { println("Moving to next floor") }
                onExit = { }
            }
            state {
                name = ElevatorState.STOPPED.name
                onEnter = { println("Stopping at floor") }
            }
            state {
                name = ElevatorState.POWER_SAVING.name
                onEnter = { println("Entering wait-mode mode") }
                onExit = { println("Leaving wait-mode mode") }
            }
            state {
                name = ElevatorState.OFF.name
                onEnter = { println("Initializing shutdown") }
                onExit = { println("Bye") }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name, ElevatorState.STOPPED.name, ElevatorState.POWER_SAVING.name)
                toState = ElevatorState.MOVING.name
                event = ElevatorEvents.FLOOR_SELECTED
                handler = { stateProcessContext -> elevator.floorSelected(stateProcessContext) }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name, ElevatorState.MOVING.name)
                toState = ElevatorState.STOPPED.name
                event = ElevatorEvents.WAITING
                handler = { stateProcessContext -> elevator.waitForFloorSelection(stateProcessContext) }
            }
            transition {
                fromStates = listOf(ElevatorState.STOPPED.name)
                toState = ElevatorState.POWER_SAVING.name
                event = ElevatorEvents.POWER_SAVE
                handler = { stateProcessContext -> elevator.sleep(stateProcessContext) }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name, ElevatorState.STOPPED.name, ElevatorState.POWER_SAVING.name)
                toState = ElevatorState.OFF.name
                event = ElevatorEvents.TURN_OFF
                handler = { stateProcessContext -> elevator.turnOff(stateProcessContext) }
            }
            transition {
                fromStates = listOf(ElevatorState.OFF.name)
                toState = ElevatorState.ON.name
                event = ElevatorEvents.TURN_ON
                handler = { stateProcessContext -> elevator.turnOn(stateProcessContext) }
            }
            transition {
                fromStates = listOf(
                    ElevatorState.ON.name,
                    ElevatorState.POWER_SAVING.name,
                    ElevatorState.STOPPED.name,
                    ElevatorState.MOVING.name
                )
                toState = ElevatorState.STOPPED.name
                event = ElevatorEvents.EMERGENCY_STOP
                handler = { stateProcessContext -> elevator.emergencyStop(stateProcessContext) }
            }
        }.create()

    private suspend fun floorSelected(stateProcessContext: StateProcessContext): StateProcessContext {
        delay(2000L)
        return stateProcessContext
    }

    private fun turnOff(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext
    private fun turnOn(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext

    private fun sleep(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext
    private fun waitForFloorSelection(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext

    private fun emergencyStop(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext

    private fun processCommandResult(commandResult: CommandResult): StateProcessContext {
        return when (val result = commandResult.result) {
            is Either.Left -> result.left
            is Either.Right -> fail(result.get().exception)
            else -> fail(IllegalArgumentException("There are only two options in an either"))
        }
    }

    protected fun fail(exception: Exception): Nothing = throw exception

    private suspend fun processCommand(elevatorCommand: ElevatorCommand): Either<StateProcessContext, Failure> {
        return stateMachine.processEvent(elevatorCommand.event, elevatorCommand.stateProcessContext)
    }

    suspend fun processEvent(event: ElevatorEvents, stateProcessContext: StateProcessContext): StateProcessContext {
        val result = processCommand(ElevatorCommand(Event(event), stateProcessContext))
        return processCommandResult(CommandResult(result))
    }

}

private enum class ElevatorState {
    ON,
    MOVING,
    STOPPED,
    POWER_SAVING,
    OFF;
}

enum class ElevatorEvents {
    TURN_ON,
    TURN_OFF,
    FLOOR_SELECTED,
    WAITING,
    POWER_SAVE,
    EMERGENCY_STOP
}


fun main() = runBlocking {
    ArbitraryRunner(Elevator("Elevator")).runElevators()
}

data class CommandResult(val result: Either<StateProcessContext, Failure>)

data class ElevatorCommand(val event: Event, val stateProcessContext: StateProcessContext)