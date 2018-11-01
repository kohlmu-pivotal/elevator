package com.elevator.state.builder

@DslMarker
annotation class StateMachineDSL

@StateMachineDSL
interface Builder {
    fun compile(): Any
    fun compile(parent: Any): Any
}