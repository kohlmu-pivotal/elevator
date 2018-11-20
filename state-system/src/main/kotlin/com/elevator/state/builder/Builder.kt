package com.elevator.state.builder

@DslMarker
annotation class StateMachineDSL

@StateMachineDSL
interface Builder {
    fun create(): Any = Unit
    fun validate()
}