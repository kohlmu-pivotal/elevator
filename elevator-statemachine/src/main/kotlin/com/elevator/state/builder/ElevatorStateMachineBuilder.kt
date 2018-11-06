package com.elevator.state.builder

class ElevatorStateMachineBuilder : StateMachineBuilder()

fun elevator(builder: ElevatorStateMachineBuilder.() -> Unit) =
    ElevatorStateMachineBuilder().apply(builder)