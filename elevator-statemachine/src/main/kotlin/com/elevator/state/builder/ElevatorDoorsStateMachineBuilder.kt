package com.elevator.state.builder

class ElevatorDoorsStateMachineBuilder : StateMachineBuilder()

fun elevatorDoors(builder: ElevatorDoorsStateMachineBuilder.() -> Unit) =
    ElevatorDoorsStateMachineBuilder().also {
        it.apply(builder)
    }