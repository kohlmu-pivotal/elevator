package com.elevator.state

import io.vavr.control.Either


class StateMachine(private val name: String, private val states: Map<String, State>, private val initialState: State) {

    fun processEvent(event: Event, stateProcessContext: StateProcessContext): StateProcessContext {
        val either: Either<StateProcessContext, Failure> =
            stateProcessContext.state.processEvent(event, context = stateProcessContext)
        when (either) {
            is Either.Left -> return either.left
            is Either.Right -> fail(either.get().exception)
            else -> fail(IllegalArgumentException("This should not happen.. It is either left or right"))
        }
    }

    fun initializeNewInstance(instanceName: String): StateProcessContext =
        StateProcessContext(instanceName, initialState, emptyMap())


    private fun fail(exception: Exception): Nothing {
        throw exception
    }
}