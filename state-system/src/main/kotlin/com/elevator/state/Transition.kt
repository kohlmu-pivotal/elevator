package com.elevator.state

class Transition(val fromState: State, val toState: State, val event: Event, val handler: () -> Unit)
