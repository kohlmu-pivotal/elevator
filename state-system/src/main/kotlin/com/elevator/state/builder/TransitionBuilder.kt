package com.elevator.state.builder

@StateMachineDSL
class TransitionBuilder : Builder {
    lateinit var fromStates: List<String>
    lateinit var toState: String
    lateinit var event: String
    lateinit var handler: () -> Unit

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