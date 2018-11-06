package com.elevator.state.builder

import com.elevator.state.Event
import com.elevator.state.transformers.BuilderToIRModelTransformer
import com.elevator.state.transformers.IRModelToStateMachineTransformer
import com.elevator.state.transformers.Transformer

abstract class StateMachineBuilder : Builder {
    lateinit var name: String
    lateinit var initialState: String
    val states: MutableMap<String, StateBuilder> = mutableMapOf()
    val transitions: MutableMap<Event, TransitionBuilder> = mutableMapOf()

    private val transformers = listOf(
        BuilderToIRModelTransformer() as Transformer<Any, Any>,
        IRModelToStateMachineTransformer() as Transformer<Any, Any>
    )

    override fun build(): Any {
        validateLateInitVars()
        return transform(this)
    }

    private tailrec fun transform(input: Any, index: Int = 0): Any =
        if (index < transformers.size) {
            val result = transformers[index].transform(input)
            transform(result, (index + 1))
        } else {
            input
        }

    protected open fun validateLateInitVars() {
        if (!this::name.isInitialized) {
            throw IllegalArgumentException("StateMachine does not contain valid name")
        }
        if (!this::initialState.isInitialized) {
            throw IllegalArgumentException("StateMachine does not contain valid initial state")
        }
    }

    fun state(builder: StateBuilder.() -> Unit) =
        StateBuilder().also {
            it.apply(builder)
            this.states[it.name] = it
        }

    fun transition(builder: TransitionBuilder.() -> Unit) =
        TransitionBuilder().also {
            it.apply(builder)
            this.transitions[Event(it.event)] = it
        }
}