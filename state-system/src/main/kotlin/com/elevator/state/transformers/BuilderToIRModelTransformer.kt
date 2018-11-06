package com.elevator.state.transformers

import com.elevator.state.Event
import com.elevator.state.builder.Builder
import com.elevator.state.builder.StateBuilder
import com.elevator.state.builder.StateMachineBuilder
import com.elevator.state.builder.TransitionBuilder
import com.elevator.state.graph.Edge
import com.elevator.state.graph.Graph
import com.elevator.state.graph.Node

class BuilderToIRModelTransformer : Transformer<Builder, IRModel> {
    override fun transform(input: Builder): IRModel {
        input.validate()
        return when (input) {
            is StateMachineBuilder -> transformStateMachineBuilder(input)
            is StateBuilder -> transformStateBuilder(input)
            is TransitionBuilder -> transformTransitionBuilder(input)
            else -> throw IllegalArgumentException("No transformer defined for ${input::class.java}")
        }
    }

    private fun transformStateMachineBuilder(input: StateMachineBuilder): Graph {
        return Graph(input.name).also { graph ->
            input.states.forEach { _, state -> graph.addNodes(transform(state) as Node) }
            input.transitions.forEach { (_, transitionBuilder) ->
                transitionBuilder.fromStates.forEach { fromState ->
                    val fromNode: Node = graph.nodes[fromState]!!
                    val toNode: Node = graph.nodes[transitionBuilder.toState]!!
                    val edge = transform(transitionBuilder) as Edge
                    edge.addToNode(toNode)
                    fromNode.addEdge(edge)
                }
            }
            graph.nodes[input.initialState]!!.also { it.initialNode = true }
        }
    }

    private fun transformStateBuilder(input: StateBuilder): Node = Node(input.name, input.onEnter, input.onExit)

    private fun transformTransitionBuilder(input: TransitionBuilder): Edge = Edge(Event(input.event), input.handler)
}









