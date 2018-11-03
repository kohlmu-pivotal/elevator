package com.elevator.state.builder

@DslMarker
annotation class StateMachineDSL

@StateMachineDSL
interface Builder {
    fun build(compile: Boolean = true): Any = Unit
}