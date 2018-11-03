package com.elevator.state.builder

import com.elevator.state.Event
import com.elevator.state.State
import com.elevator.state.StateMachine
import com.elevator.state.Transition
import com.elevator.state.graph.Graph
import com.elevator.state.graph.Node

abstract class StateMachineBuilder : Builder {
    lateinit var name: String
    lateinit var initialState: String
    val states: MutableMap<String, StateBuilder> = mutableMapOf()
    val transitions: MutableMap<Event, TransitionBuilder> = mutableMapOf()

    private lateinit var graph: Graph

    fun compile(): Graph {
        validateLateInitVars()
        graph = Graph(name).also { graph ->
            states.forEach { _, state -> graph.addNodes(state.compile(graph) as Node) }
            transitions.forEach { (_, transitionBuilder) ->
                transitionBuilder.fromStates.forEach { fromState ->
                    val fromNode: Node = graph.nodes[fromState]!!
                    val toNode: Node = graph.nodes[transitionBuilder.toState]!!
                    fromNode.addEdge(transitionBuilder.compile(toNode))
                }
            }
            graph.nodes[initialState]!!.also { it.initialNode = true }
        }
        return graph
    }

    fun compile(parent: Any): Any = compile()

    override fun build(compile: Boolean): Any {
        graph.nodes.forEach { _, node ->
            val stateBuilder = states[node.name]!!
            node.edges.forEach { event, edge -> Transition() }
            State(node.name, stateBuilder.onEnter, stateBuilder.onExit, node.e)
        }
        StateMachine(name,)
    }

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
            this.states[it.name] = it
        }

    fun transition(builder: TransitionBuilder.() -> Unit) =
        TransitionBuilder().also {
            it.apply(builder)
            this.transitions[Event(it.event)] = it
        }
}