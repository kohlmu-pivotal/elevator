package com.elevator.state

data class State(
    val name: String,
    val onEnter: () -> Unit,
    val onExit: () -> Unit,
    private val availableActions: Map<Event, Transition>
) {

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = other is State && other.name == name

    fun processEvent(context: StateProcessContext): StateProcessContext {
        availableActions[context.event]?.handler?.invoke()
            ?: throw IllegalArgumentException("event ${context.event} is not supported by state $name")
        return context.copy(event = context.event, contextParameters = context.contextParameters)
    }
}

