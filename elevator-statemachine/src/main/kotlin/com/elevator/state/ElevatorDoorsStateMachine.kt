package com.elevator.state

class ElevatorDoorsStateMachine(name: String, states: Map<String, State>, initialState: State) :
    StateMachine(name, states, initialState) {

    enum class ElevatorDoorState(name: String) {
        OPEN("Open"),
        CLOSED("Closed"),
        JAMMED("JAMMED")
    }

    enum class ElevatorDoorEvent(name: String) {
        OPEN_EVENT("opening"),
        CLOSE_EVENT("closing"),
        JAMMED_EVENT("jamming"),
        FIXED_EVENT("fixing")
    }
}
