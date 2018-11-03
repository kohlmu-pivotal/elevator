package com.elevator.state.builder

import com.elevator.state.graph.Node


sealed class StateBuilder : Builder {
    lateinit var name: String
    lateinit var onEnter: () -> Unit
    lateinit var onExit: () -> Unit

    protected open fun validateLateInitVars() {
        if (!this::name.isInitialized) {
            throw IllegalArgumentException("State does not contain valid name")
        }
        if (!this::onEnter.isInitialized) {
            onEnter = {}
        }
        if (!this::onExit.isInitialized) {
            onExit = {}
        }
    }

    class SimpleStateBuilder : StateBuilder() {
        override fun compile(): Any {
            validateLateInitVars()
            return Node(name)
        }

        override fun compile(parent: Any): Any = compile() as Node
    }
}