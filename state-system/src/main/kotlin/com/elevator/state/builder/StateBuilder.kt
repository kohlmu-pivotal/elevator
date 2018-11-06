package com.elevator.state.builder


class StateBuilder : Builder {
    lateinit var name: String
    lateinit var onEnter: () -> Unit
    lateinit var onExit: () -> Unit

    override fun validate() {
        if (!this::name.isInitialized) {
            throw IllegalArgumentException("State does not contain valid name")
        }
        if (!this::onEnter.isInitialized) {
            onEnter = {}
        }
        if (!this::onExit.isInitialized) {
            onExit = {}
        }
    }
}