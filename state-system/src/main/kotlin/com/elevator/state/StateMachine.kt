package com.elevator.state

import io.vavr.collection.HashMap
import io.vavr.control.Either


class StateMachine(private val name: String, private val states: Map<String, State>, private val initialState: State) {

    fun initializeNewInstance(instanceName: String): StateProcessContext =
        StateProcessContext(instanceName, initialState, HashMap.empty())

    suspend fun processEvent(event: Event, context: StateProcessContext): Either<StateProcessContext, Failure> {
        val currentState = context.state
        val transition = currentState.availableTransitions[event]
        return transition?.handler
            ?.run {
                val newStateProcessContext = invoke(context)
                val newContextParameters = context.contextParameters.put(currentState.name, newStateProcessContext)
                return Either.left(context.copy(contextParameters = newContextParameters, state = transition.toState))
            }
            ?: run { return Either.right(Failure(IllegalArgumentException("${context.instanceName} - event $event is not supported by state $name"))) }
    }
}