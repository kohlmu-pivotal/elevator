package com.elevator.state.core

import com.elevator.state.StateProcessContext
import com.elevator.state.builder.elevatorDoors

class ElevatorDoors(private val elevator: Elevator) {
    private val statemachine = initializeStateMachine(this)

    private fun initializeStateMachine(elevatorDoors: ElevatorDoors) =
        elevatorDoors {
            name = "ElevatorDoors StateMachine"
            initialState = ElevatorDoorState.OPEN.name
            state {
                name = ElevatorDoorState.OPEN.name
                onEnter = { println("Opening doors") }
                onExit = { println("Closing doors") }
            }
            state {
                name = ElevatorDoorState.JAMMED.name
                onEnter = { println("Jamming") }
                onExit = { println("Fixed") }
            }
            state {
                name = ElevatorDoorState.CLOSED.name
                onEnter = { println("Closing doors") }
                onExit = { println("Opening doors") }
            }
            transition {
                event = ElevatorDoorEvent.OPEN_EVENT
                fromStates = listOf(ElevatorDoorState.CLOSED.name)
                toState = ElevatorDoorState.OPEN.name
                handler = { stateProcessContext -> elevatorDoors.open(stateProcessContext) }
            }
            transition {
                event = ElevatorDoorEvent.FIXED_EVENT
                fromStates = listOf(ElevatorDoorState.JAMMED.name)
                toState = ElevatorDoorState.OPEN.name
                handler = { stateProcessContext -> elevatorDoors.fixJam(stateProcessContext) }
            }
            transition {
                event = ElevatorDoorEvent.CLOSE_EVENT
                fromStates = listOf(ElevatorDoorState.OPEN.name)
                toState = ElevatorDoorState.CLOSED.name
                handler = { stateProcessContext -> elevatorDoors.close(stateProcessContext) }
            }
            transition {
                event = ElevatorDoorEvent.JAMMED_EVENT
                fromStates = listOf(ElevatorDoorState.OPEN.name, ElevatorDoorState.CLOSED.name)
                toState = ElevatorDoorState.JAMMED.name
                handler = { stateProcessContext -> elevatorDoors.jammed(stateProcessContext) }
            }
        }.create()

    private fun close(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext
    private fun open(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext
    private fun jammed(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext
    private fun fixJam(stateProcessContext: StateProcessContext): StateProcessContext = stateProcessContext
}

private enum class ElevatorDoorState(name: String) {
    OPEN("Open"),
    CLOSED("Closed"),
    JAMMED("JAMMED")
}

private enum class ElevatorDoorEvent(name: String) {
    OPEN_EVENT("opening"),
    CLOSE_EVENT("closing"),
    JAMMED_EVENT("jamming"),
    FIXED_EVENT("fixing")
}
