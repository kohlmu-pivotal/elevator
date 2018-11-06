package com.elevator.state

class Transition(val toState: State, val event: Event, val handler: () -> Unit)
