package com.elevator.state.graph

data class Graph(val name: String) {
    val nodes: MutableMap<String, Node> = mutableMapOf()
    fun addNodes(node: Node) {
        nodes[node.name] = node
    }
}

data class Node(val name: String) {
    val edges: MutableMap<String, Edge> = mutableMapOf()
    var initialNode: Boolean = false

    fun addEdge(edge: Edge) {
        edges[edge.event] = edge
    }
}

data class Edge(val event: String, val toNode: Node, val sideEffect: () -> Unit)