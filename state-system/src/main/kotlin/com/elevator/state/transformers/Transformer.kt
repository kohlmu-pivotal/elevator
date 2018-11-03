package com.elevator.state.transformers

interface Transformer<IN, OUT> {
    fun transform(input: IN): OUT
}
