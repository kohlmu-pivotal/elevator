package com.elevator.state.transformers

import com.elevator.state.Event
import com.elevator.state.State
import com.elevator.state.StateMachine
import com.elevator.state.Transition
import com.elevator.state.graph.Edge
import com.elevator.state.graph.Graph
import com.elevator.state.graph.Node

class IRModelToStateMachineTransformer : Transformer<IRModel, Any> {
    override fun transform(input: IRModel): Any {
        return when (input) {
            is Graph -> transformGraph(input)
            is Node -> transformNode(input)
            is Edge -> transformEdge(input)
            else -> throw IllegalArgumentException("No transformer for type ${input::class.java} exists")
        }
    }

    private fun transformEdge(input: Edge, toState: State = transformNode(input.toNode)): Transition =
        Transition(toState, Event(input.event), input.sideEffect)

    private fun transformNode(input: Node): State = State(input.name, input.onEnter, input.onExit, emptyMap())

    private fun transformGraph(input: Graph): StateMachine {
        val stateMap = input.nodes.map { (name, node) -> Pair(name, transform(node)) }.toMap()
    }

    private fun mapEdgesToState(state: State, edges: List<Edge>): MutableMap<String, State> {
        val mapOfStates = mutableMapOf<String, State>()
        mapOfStates[state.name] = state
        val transitions =
            edges.map { edge ->
                val toNode = edge.toNode
                val toState = mapOfStates[toNode.name]
                toState
                    ?.apply { transformEdge(edge, toState) }
                    ?: apply {
                        val toState = transformNode(toNode)
                        mapOfStates[toState.name] = toState
                        mapOfStates.putAll(mapEdgesToState(toState, toNode.edges.values.toList()))
                        transformEdge(edge, toState)
                    }
            }.toList()
        state.return mapOfStates
    }

}