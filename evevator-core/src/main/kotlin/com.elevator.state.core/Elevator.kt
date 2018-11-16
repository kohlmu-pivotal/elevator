package com.elevator.state.core

import com.elevator.state.Event
import com.elevator.state.StateMachine
import com.elevator.state.StateProcessContext
import com.elevator.state.builder.elevator

class Elevator(val elevatorIdentifier: String) {

    private val stateMachine: StateMachine = initializeElevatorStateMachine(this)
    private val elevatorDoors: ElevatorDoors = ElevatorDoors(this)
    private val controlPanel: ControlPanel = ControlPanel(this)

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
                handler = { elevator.floorSelected() }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name, ElevatorState.MOVING.name)
                toState = ElevatorState.STOPPED.name
                event = ElevatorEvents.WAITING
                handler = { elevator.waitForFloorSelection() }
            }
            transition {
                fromStates = listOf(ElevatorState.STOPPED.name)
                toState = ElevatorState.POWER_SAVING.name
                event = ElevatorEvents.POWER_SAVE
                handler = { elevator.sleep() }
            }
            transition {
                fromStates = listOf(ElevatorState.ON.name, ElevatorState.STOPPED.name, ElevatorState.POWER_SAVING.name)
                toState = ElevatorState.OFF.name
                event = ElevatorEvents.TURN_OFF
                handler = { elevator.turnOff() }
            }
            transition {
                fromStates = listOf(ElevatorState.OFF.name)
                toState = ElevatorState.ON.name
                event = ElevatorEvents.TURN_ON
                handler = { elevator.turnOn() }
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
                handler = { elevator.emergencyStop() }
            }
        }.build()

    private fun floorSelected() {
        println("Floor Selected")
        Thread.sleep(2000)
    }

    private fun turnOff() {}
    private fun turnOn() {}
    private fun sleep() {}
    private fun waitForFloorSelection() {}
    private fun emergencyStop() {}

    fun createNewInstance(instanceName: String): StateProcessContext = stateMachine.initializeNewInstance(instanceName)

    fun turnOn(stateProcessContext: StateProcessContext): StateProcessContext =
        stateMachine.processEvent(Event(ElevatorEvents.TURN_ON), stateProcessContext)

    fun pressButton(stateProcessContext: StateProcessContext): StateProcessContext =
        stateMachine.processEvent(Event(ElevatorEvents.FLOOR_SELECTED), stateProcessContext)
}

private enum class ElevatorState {
    ON,
    MOVING,
    STOPPED,
    POWER_SAVING,
    OFF;
}

private enum class ElevatorEvents {
    TURN_ON,
    TURN_OFF,
    FLOOR_SELECTED,
    WAITING,
    POWER_SAVE,
    EMERGENCY_STOP
}

fun main(args: Array<String>) {
    val elevator = Elevator("Elevator")
    var e1Instance = elevator.createNewInstance("E1")
    var e2Instance = elevator.createNewInstance("E2")
    e1Instance = elevator.turnOn(e1Instance)
//    e2Instance = elevator.pressButton(e2Instance)
    e1Instance = elevator.pressButton(e1Instance)
    println()
}