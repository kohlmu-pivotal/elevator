package com.elevator.state

class ElevatorStateMachine(
    name: String, states: Map<String, State>, transitions: Map<Event, Transition>, initialState: State
) : StateMachine(name, states, transitions, initialState) {

    enum class ElevatorState(name: String) {
        ON("On"),
        MOVING("Moving"),
        STOPPED("Stopped"),
        POWER_SAVING("Power Saving"),
        OFF("Off");
    }

    enum class ElevatorEvents(name: String) {
        TURN_ON("turn on"),
        TURN_OFF("turn off"),
        FLOOR_SELECTED("floor selected"),
        WAITING("waiting"),
        POWER_SAVE("power save"),
        EMERGENCY_STOP("Emergency STOP")
    }
}