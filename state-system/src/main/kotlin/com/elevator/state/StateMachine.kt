package com.elevator.state


open class StateMachine(
    private val name: String, private val states: Map<String, State>, private val initialState: State
) {
    private var currentState: State = initialState
        set(value) {
            if (currentState === value) {
                return
            }

            currentState.onExit.invoke()
            field = value
            currentState.onEnter.invoke()
        }

    init {
        currentState = initialState
        initialState.onEnter.invoke()
    }

    fun processEvent(stateProcessContext: StateProcessContext): StateProcessContext {
        return currentState.processEvent(context = stateProcessContext)
    }
}