package com.elevator.state.builder

import metatype.deepstate.kt.dsl.StateMachineDSL

@StateMachineDSL
interface Builder<T> {
    fun build(): T = build(this)
    fun build(builder: Builder<*> = this): T
}