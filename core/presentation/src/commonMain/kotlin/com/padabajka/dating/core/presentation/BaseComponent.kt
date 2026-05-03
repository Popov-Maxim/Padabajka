package com.padabajka.dating.core.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStop
import com.padabajka.dating.core.presentation.error.DomainErrorHandler
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseComponent<T : State>(
    context: ComponentContext,
    screenName: String,
    initialState: T,
    koinComponent: KoinComponent = object : KoinComponent {},
    private val domainErrorHandler: DomainErrorHandler = koinComponent.get(),
    private val lifecycleListener: ComponentLifecycleListener = koinComponent.get {
        parametersOf(screenName)
    }
) :
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
        context.lifecycle.doOnResume {
            lifecycleListener.onResume()
        }
        context.lifecycle.doOnPause {
            lifecycleListener.onPause()
        }
        context.lifecycle.doOnStop {
            onStopped()
        }
    }

    protected fun reduce(update: (state: T) -> T): Job = reducerScope.launch {
        _state.update(update)
    }

    @Suppress("TooGenericExceptionCaught")
    protected fun launchStep(
        action: suspend () -> Unit,
        onError: suspend (ExternalDomainError) -> Boolean = { false }
    ): Job = componentScope.launch {
        try {
            action()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            domainErrorHandler.handle(e, onError)
        }
    }

    open fun onStopped() {}
}
