package com.elevator.state.builder

import com.elevator.state.StateProcessContext

@StateMachineDSL
class TransitionBuilder : Builder {
    lateinit var fromStates: List<String>
    lateinit var toState: String
    lateinit var event: Any
    lateinit var handler: suspend (StateProcessContext) -> StateProcessContext

    override fun validate() {
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
            handler = { stateProcessContext -> stateProcessContext }
        }
    }
}