package com.elevator.state

data class Transition(val toState: State, val event: Event, val handler: () -> Unit)
