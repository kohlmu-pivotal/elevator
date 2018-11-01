package com.elevator.state

data class StateProcessContext(var event: Any, val contextParameters: Map<Any, Any>)