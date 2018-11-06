package com.elevator.state.core

import com.elevator.state.ElevatorDoorsStateMachine.ElevatorDoorEvent
import com.elevator.state.ElevatorDoorsStateMachine.ElevatorDoorState
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
                handler = { elevatorDoors.open() }
            }
            transition {
                event = ElevatorDoorEvent.FIXED_EVENT
                fromStates = listOf(ElevatorDoorState.JAMMED.name)
                toState = ElevatorDoorState.OPEN.name
                handler = { elevatorDoors.fixJam() }
            }
            transition {
                event = ElevatorDoorEvent.CLOSE_EVENT
                fromStates = listOf(ElevatorDoorState.OPEN.name)
                toState = ElevatorDoorState.CLOSED.name
                handler = { elevatorDoors.close() }
            }
            transition {
                event = ElevatorDoorEvent.JAMMED_EVENT
                fromStates = listOf(ElevatorDoorState.OPEN.name, ElevatorDoorState.CLOSED.name)
                toState = ElevatorDoorState.JAMMED.name
                handler = { elevatorDoors.jammed() }
            }
        }.build()

    private fun close() {}
    private fun open() {}
    private fun jammed() {}
    private fun fixJam() {}
}
