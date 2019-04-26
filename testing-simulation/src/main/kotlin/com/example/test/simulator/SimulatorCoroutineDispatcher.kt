package com.example.test.simulator

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Runnable
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

class SimulatorCoroutineDispatcher: ExecutorCoroutineDispatcher() {
    override val executor: Executor
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}