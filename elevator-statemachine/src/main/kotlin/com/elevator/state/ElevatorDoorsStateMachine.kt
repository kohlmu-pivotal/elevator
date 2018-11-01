package com.elevator.state

class ElevatorDoorsStateMachine(
    name: String, states: Map<String, State>, transitions: Map<Event, Transition>, initialState: State
) : StateMachine(name, states, transitions, initialState) {

    enum class ElevatorDoorState(name: String) {
        OPEN("Open"), CLOSED("Closed"), JAMMED("JAMMED")
    }

    enum class ElevatorDoorEvent(name: String) {
        OPEN_EVENT("opening"), CLOSE_EVENT("closing"),
        JAMMED_EVENT("jamming"), FIXED_EVENT("fixing")
    }
}
