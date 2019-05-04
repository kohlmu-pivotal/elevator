package com.elevator.state

data class State(val name: String, val onEnter: () -> Unit,
                 val onExit: () -> Unit, val availableTransitions: Map<Event, Transition>) {

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?) = other is State && other.name == name
}