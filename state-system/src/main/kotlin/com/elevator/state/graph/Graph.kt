package com.elevator.state.graph

import com.elevator.state.Event
import com.elevator.state.transformers.IRModel

data class Graph(val name: String) : IRModel {
    val nodes: MutableMap<String, Node> = mutableMapOf()
    fun addNodes(node: Node) {
        nodes[node.name] = node
    }
}

data class Node(val name: String, val onEnter: () -> Unit, val onExit: () -> Unit) : IRModel {
    val edges: MutableMap<Event, Edge> = mutableMapOf()
    var initialNode: Boolean = false

    fun addEdge(edge: Edge) {
        edges[edge.event] = edge
    }
}

data class Edge(val event: Event, val sideEffect: () -> Unit) : IRModel {
    lateinit var toNode: Node

    fun addToNode(toNode: Node) {
        this.toNode = toNode
    }
}