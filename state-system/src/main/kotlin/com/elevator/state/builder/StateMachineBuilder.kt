package com.elevator.state.builder

import com.elevator.state.Event
import com.elevator.state.graph.Graph
import com.elevator.state.graph.Node

abstract class StateMachineBuilder : Builder {
    lateinit var name: String
    lateinit var initialState: String
    val states: MutableSet<StateBuilder> = mutableSetOf()
    val transitions: MutableMap<Event, TransitionBuilder> = mutableMapOf()

    override fun compile(): Graph {
        validateLateInitVars()
        return Graph(name).also { graph ->
            states.forEach { state -> graph.addNodes(state.compile(graph) as Node) }
            transitions.forEach { (_, transitionBuilder) ->
                transitionBuilder.fromStates.forEach { fromState ->
                    val fromNode: Node = graph.nodes[fromState]!!
                    val toNode: Node = graph.nodes[transitionBuilder.toState]!!
                    fromNode.addEdge(transitionBuilder.compile(toNode))
                }
            }
            graph.nodes[initialState]!!.also { it.initialNode = true }
        }
    }

    override fun compile(parent: Any): Any = compile()

    protected open fun validateLateInitVars() {
        if (!this::name.isInitialized) {
            throw IllegalArgumentException("StateMachine does not contain valid name")
        }
        if (!this::initialState.isInitialized) {
            throw IllegalArgumentException("StateMachine does not contain valid initial state")
        }
    }

    fun state(builder: StateBuilder.SimpleStateBuilder.() -> Unit) =
        StateBuilder.SimpleStateBuilder().also {
            it.apply(builder)
            this.states.add(it)
        }

    fun transition(builder: TransitionBuilder.() -> Unit) =
        TransitionBuilder().also {
            it.apply(builder)
            this.transitions[Event(it.event)] = it
        }
}