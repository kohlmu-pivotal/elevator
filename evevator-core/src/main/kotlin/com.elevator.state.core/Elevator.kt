package com.elevator.state.core

import com.elevator.state.ElevatorStateMachine.ElevatorEvents
import com.elevator.state.ElevatorStateMachine.ElevatorState
import com.elevator.state.builder.elevator

class Elevator(val elevatorIdentifier: String) {

    private val stateMachine = initializeElevatorStateMachine(this)
    private val elevatorDoors: ElevatorDoors = ElevatorDoors(this)
    private val controlPanel: ControlPanel = ControlPanel(this)

    private fun initializeElevatorStateMachine(elevator: Elevator) =
        elevator {
            name = elevatorIdentifier
            initialState = ElevatorState.ON.name
            state {
                name = ElevatorState.ON.name
                onEnter = { println("Initializing Elevator.....") }
                onExit = { println("Initialized....\n Ready") }
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
                event = ElevatorEvents.FLOOR_SELECTED.name
                handler = { elevator.floorSelected() }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name)
                toState = ElevatorState.STOPPED.name
                event = ElevatorEvents.WAITING.name
                handler = { elevator.waitForFloorSelection() }
            }
            transition {
                fromStates = listOf(ElevatorState.STOPPED.name)
                toState = ElevatorState.POWER_SAVING.name
                event = ElevatorEvents.POWER_SAVE.name
                handler = { elevator.sleep() }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name, ElevatorState.STOPPED.name, ElevatorState.POWER_SAVING.name)
                toState = ElevatorState.OFF.name
                event = ElevatorEvents.TURN_OFF.name
                handler = { elevator.turnOff() }
            }
            transition {
                fromStates = listOf(ElevatorState.OFF.name)
                toState = ElevatorState.ON.name
                event = ElevatorEvents.TURN_ON.name
                handler = { elevator.turnOn() }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name, ElevatorState.POWER_SAVING.name, ElevatorState.STOPPED.name)
                toState = ElevatorState.STOPPED.name
                event = ElevatorEvents.EMERGENCY_STOP.name
                handler = { elevator.emergencyStop() }
            }
        }.compile()

    private fun floorSelected() {}
    private fun turnOff() {}
    private fun turnOn() {}
    private fun sleep() {}
    private fun waitForFloorSelection() {}
    private fun emergencyStop() {}
}

fun main(args: Array<String>) {
    val elevator = Elevator("TestElevator")
    println()
}