package com.elevator.state.builder

import com.elevator.state.graph.Edge
import com.elevator.state.graph.Node

@StateMachineDSL
class TransitionBuilder : Builder {
    lateinit var fromStates: List<String>
    lateinit var toState: String
    lateinit var event: String
    lateinit var handler: () -> Unit

    override fun compile(): Any {
        TODO("This method is not to be used, due to the reliance on the toState being populated")
    }

    override fun compile(parent: Any): Edge {
        validateVars()
        return Edge(event, parent as Node, handler)
    }

    private fun validateVars() {
        if (!this::fromStates.isInitialized) {
            throw IllegalArgumentException("No fromState defined for transition}")
        }
        if (!this::toState.isInitialized) {
            throw IllegalArgumentException("No toState defined for transition}")
        }
        if (!this::event.isInitialized) {
            throw IllegalArgumentException("No event defined for transition}")
        }
        if (!this::handler.isInitialized) {
            handler = {}
        }
    }
}