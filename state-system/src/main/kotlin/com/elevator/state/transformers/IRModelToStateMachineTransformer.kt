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
            else -> throw IllegalArgumentException("No transformer for type ${input::class.java} exists")
        }
    }

    private fun transformEdge(input: Edge, toState: State): Transition =
        Transition(toState, input.event, input.sideEffect)

    private fun transformNode(input: Node, statesMap: MutableMap<String, State>): Pair<State, Boolean> {
        val inputState = State(input.name, input.onEnter, input.onExit, emptyMap())
        statesMap[inputState.name] = inputState
        return Pair(mapEdgesToState(
            inputState,
            statesMap,
            input.edges.values.toList()
        ).also {
            statesMap[it.name] = it
        }, input.initialNode
        )
    }

    private fun transformGraph(input: Graph): StateMachine {
        val statesMap = mutableMapOf<String, State>()

        val initialState = input.nodes.filter { (name, _) -> !statesMap.containsKey(name) }
            .map { (_, node) ->
                val statePair = transformNode(node, statesMap)
                statePair
            }.filter { (state, initialState) -> initialState }.map { (state, _) -> state }.first()

        return StateMachine(input.name, statesMap, initialState)
    }

    private fun mapEdgesToState(state: State, mapOfStates: MutableMap<String, State>, edges: List<Edge>): State {
        val transitions: List<Transition> =
            edges.map { edge ->
                val toNode = edge.toNode
                mapOfStates[toNode.name]
                    ?.run { transformEdge(edge, this) }
                    ?: run {
                        val toState = transformNode(toNode, mapOfStates).also {
                            mapOfStates[it.first.name] = it.first
//                            mapEdgesToState(it.first, mapOfStates, toNode.edges.values.toList())
                        }.first
                        transformEdge(edge, toState)
                    }
            }.toList()
        val transitionMap = mutableMapOf<Event, Transition>()
        transitions.associateByTo(transitionMap) { it.event }
        return state.copy(availableActions = transitionMap.toMap())
    }

}