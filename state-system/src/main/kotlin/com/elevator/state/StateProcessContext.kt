package com.elevator.state

data class StateProcessContext(var event: Event, val contextParameters: Map<Any, Any>)