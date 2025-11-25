package com.padabajka.dating.core.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseComponent<T : State>(context: ComponentContext, initialState: T) :
    ComponentContext by context {

    protected val componentScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val reducerScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableValue(initialState)
    val state: Value<T> = _state

    init {
        context.lifecycle.doOnDestroy {
            componentScope.cancel("Component destroyed")
        }
        context.lifecycle.doOnStop { onStopped() }
    }

    protected fun reduce(update: (state: T) -> T): Job = reducerScope.launch {
        _state.update(update)
    }

    // Should be inlined but doesn't work from other modules. Looks like kotlin bug
    @Suppress("TooGenericExceptionCaught")
    protected fun <M> mapAndReduceException(
        action: suspend () -> Unit,
        mapper: (Throwable) -> M,
        update: (T, M?) -> T
    ): Job = componentScope.launch {
        var mappedException: M? = null

        try {
            action()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            println("${this::class.simpleName} exception in mapAndReduce: ${e.message}")
            mappedException = mapper(e)
        }

        reduce { update(it, mappedException) }
    }

    open fun onStopped() {}
}
