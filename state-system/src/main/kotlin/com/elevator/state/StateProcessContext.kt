package com.elevator.state

data class StateProcessContext internal constructor(val instanceName: String, val state: State,
                                                    val contextParameters: io.vavr.collection.Map<Any, Any>)