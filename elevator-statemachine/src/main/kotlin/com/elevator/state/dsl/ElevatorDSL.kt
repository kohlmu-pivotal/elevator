package com.elevator.state.dsl

import com.elevator.state.builder.ElevatorDoorsStateMachineBuilder
import com.elevator.state.builder.ElevatorStateMachineBuilder

fun elevator(builder: ElevatorStateMachineBuilder.() -> Unit) =
    ElevatorStateMachineBuilder().also {
        it.apply(builder)
    }

fun elevatorDoors(builder: ElevatorDoorsStateMachineBuilder.() -> Unit) =
    ElevatorDoorsStateMachineBuilder().also {
        it.apply(builder)
    }