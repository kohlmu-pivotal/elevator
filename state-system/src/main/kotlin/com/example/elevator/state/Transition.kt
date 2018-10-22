package com.example.elevator.state

class Transition(val fromState: State, val toState: State, val trigger: Event, val handler: () -> Unit)
