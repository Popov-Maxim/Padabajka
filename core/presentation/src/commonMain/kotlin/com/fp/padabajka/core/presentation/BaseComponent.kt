package com.fp.padabajka.core.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.getAndUpdate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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

    protected fun reduce(update: (T) -> T) = componentScope.launch {
        reducerMutex.withLock {
            _state.getAndUpdate(update)
        }
    }
}