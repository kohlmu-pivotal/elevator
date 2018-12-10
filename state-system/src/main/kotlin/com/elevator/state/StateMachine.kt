package com.elevator.state


class StateMachine(private val name: String, private val states: Map<String, State>, private val initialState: State) {

    suspend fun processEvent(event: Event, stateProcessContext: StateProcessContext) =
        stateProcessContext.state.processEvent(event, stateProcessContext)

    fun initializeNewInstance(instanceName: String): StateProcessContext =
        StateProcessContext(instanceName, initialState, emptyMap())
}