package com.fp.padabajka.core.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.getAndUpdate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
        reducerMutex.withLock {
            _state.getAndUpdate(update)
        }
    }

    @Suppress("CheckedExceptionsKotlin")
    protected inline fun <reified S : T> reduce(crossinline update: (S) -> T): Job =
        reduce { state ->
            if (state is S) {
                update(state)
            } else {
                if (isDebugBuild()) {
                    throw UnexpectedStateException(
                        "Unexpected State type ${state::class.simpleName} where ${S::class.simpleName} is expected!"
                    )
                }
                state
            }
        }

    @Suppress("TooGenericExceptionCaught")
    protected inline fun <M> mapAndReduceException(
        crossinline action: suspend () -> Unit,
        crossinline mapper: (Throwable) -> M,
        crossinline update: (T, M?) -> T
    ): Job = componentScope.launch {
        var mappedException: M? = null

        try {
            action()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            mappedException = mapper(e)
        }

        reduce { update(it, mappedException) }
    }
}
