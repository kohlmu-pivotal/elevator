package com.elevator.state


open class StateMachine(
    private val name: String, private val states: Map<String, State>,
                   private val transitions: Map<Event, Transition>, private val initialState: State
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

    fun processEvent(event: Event) {
        transitions[event]?.run {
            if (this.fromState === currentState) {
                this.handler.invoke()
                currentState = this.toState
            }
        } ?: throw IllegalStateException("No event exists for ${event.name}")
    }
}