package com.elevator.state

data class State(
    val name: String,
    val onEnter: () -> Unit,
    val onExit: () -> Unit,
    private val availableActions: Map<Event, Transition>
) {

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = other is State && other.name == name

    fun processEvent(event: Event, context: StateProcessContext): StateProcessContext {
        val transition = availableActions[event]
        transition?.handler?.invoke()
            ?: throw IllegalArgumentException("event $event is not supported by state $name")
        return context.copy(
            contextParameters = context.contextParameters,
            state = transition.toState
        )
    }
}

