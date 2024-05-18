package com.fp.padabajka.core.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseComponent<T : State>(context: ComponentContext, initialState: T) :
    ComponentContext by context {

    protected val componentScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val reducerMutex = Mutex(false)

    private val _state = MutableValue(initialState)
    val state: Value<T> = _state

    init {
        context.lifecycle.doOnDestroy {
            componentScope.cancel("Component destroyed")
        }
    }

    protected fun reduce(update: (T) -> T): Job = componentScope.launch {
        reduceBlocking(update)
    }

    @OptIn(ExperimentalContracts::class)
    protected suspend fun reduceBlocking(update: (T) -> T) {
        contract {
            callsInPlace(update, InvocationKind.EXACTLY_ONCE)
        }
        reducerMutex.withLock {
            _state.update(update)
        }
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
            mappedException = mapper(e)
        }

        reduceBlocking { update(it, mappedException) }
    }
}
