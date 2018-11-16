package com.elevator.state


class StateMachine(
    private val name: String, private val states: Map<String, State>, private val initialState: State
) {

    fun processEvent(event: Event, stateProcessContext: StateProcessContext): StateProcessContext =
        stateProcessContext.state.processEvent(event, context = stateProcessContext)

    fun initializeNewInstance(instanceName: String): StateProcessContext =
        StateProcessContext(instanceName, initialState, emptyMap())
}