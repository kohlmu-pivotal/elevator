package com.elevator.state.builder

@DslMarker
annotation class StateMachineDSL

@StateMachineDSL
interface Builder {
    fun build(): Any = Unit
    fun validate()
}