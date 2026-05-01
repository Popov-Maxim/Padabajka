package com.padabajka.dating.core.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStop
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.exception.BadStatusCodeException
import com.padabajka.dating.core.repository.api.exception.ConnectException
import com.padabajka.dating.core.repository.api.exception.UserException
import com.padabajka.dating.core.utils.isDebugBuild
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
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
        onError: (ExternalDomainError) -> Boolean = { false }
    ): Job = componentScope.launch {
        try {
            action()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            val mappedError = mapError(e)
            when (mappedError) {
                is ExternalDomainError -> handleExternalError(mappedError, e, onError)
                is InternalDomainError -> handleInternalError(mappedError)
            }
        }
    }

    private fun handleInternalError(error: InternalDomainError) {
        when (error) {
            is InternalDomainError.User -> TODO()
        }
    }

    private fun handleExternalError(
        error: ExternalDomainError,
        e: Throwable,
        onError: (ExternalDomainError) -> Boolean = { false }
    ) {
        val handled = onError(error)
        if (handled.not() && error.needLog) {
            if (isDebugBuild) {
                throw e
            } else {
                Firebase.crashlytics.recordException(e)
            }
        }
    }

    open fun onStopped() {}
}

sealed interface DomainError

sealed interface InternalDomainError : DomainError {
    data class User(val error: UserException) : InternalDomainError
}

sealed class ExternalDomainError(val needLog: Boolean) : DomainError {

    data class TextError(val text: StaticTextId) : ExternalDomainError(false) {
        companion object {
            val Internet = TextError(StaticTextId.UiId.InternetConnectionErrorDescription)
            val BadStatusCode = TextError(StaticTextId.UiId.UnknownErrorDescription)
        }
    }

    data class Unknown(val e: Throwable) : ExternalDomainError(true)
}

private fun mapError(e: Throwable): DomainError {
    return when (e) {
        is ConnectException -> ExternalDomainError.TextError.Internet
        is UserException -> InternalDomainError.User(e)
        is BadStatusCodeException -> ExternalDomainError.TextError.BadStatusCode
        else -> ExternalDomainError.Unknown(e)
    }
}
