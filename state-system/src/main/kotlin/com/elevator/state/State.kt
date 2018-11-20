package com.elevator.state

import io.vavr.control.Either

data class State(
    val name: String,
    val onEnter: () -> Unit,
    val onExit: () -> Unit,
    private val availableActions: Map<Event, Transition>
) {

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = other is State && other.name == name

    fun processEvent(event: Event, context: StateProcessContext): Either<StateProcessContext, Failure> {
        val transition = availableActions[event]
        return transition?.handler
            ?.run {
                invoke()
                return Either.left(
                    context.copy(contextParameters = context.contextParameters, state = transition.toState)
                )
            }
            ?: run { return Either.right(Failure(IllegalArgumentException("event $event is not supported by state $name"))) }
    }
}