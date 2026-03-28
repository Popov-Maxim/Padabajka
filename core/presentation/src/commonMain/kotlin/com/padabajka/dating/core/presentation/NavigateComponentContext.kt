package com.padabajka.dating.core.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import org.koin.core.component.KoinComponent

abstract class NavigateComponentContext<Config : Any, Child : Any>(
    context: ComponentContext,
    serializable: KSerializer<Config>,
    initialConfiguration: Config,
    private val splash: Config? = null
) : ComponentContext by context, KoinComponent {

    private val navigation = StackNavigation<Config>()
    val childStack by lazy {
        childStack(
            source = navigation,
            serializer = serializable,
            initialConfiguration = initialConfiguration,
            handleBackButton = true,
            childFactory = ::createChild
        )
    }

    protected val navigateScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    protected val backgroundScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    protected fun navigate(configuration: Config, saveLast: Boolean = true) {
        navigateScope.launch {
            navigation.navigate { stack ->
                val stackWithoutSplash = (splash?.let { stack - it } ?: stack)
                val stackWithLastCorrected = if (saveLast.not()) {
                    stackWithoutSplash.dropLast(1)
                } else {
                    stackWithoutSplash
                }
                stackWithLastCorrected - configuration + configuration
            }
        }
    }

    protected fun navigateNewStack(configuration: Config) {
        navigateScope.launch {
            navigation.navigate {
                listOf(configuration)
            }
        }
    }

    protected fun navigateBack() {
        navigateScope.launch {
            navigation.pop()
        }
    }

    protected abstract fun createChild(
        configuration: Config,
        context: ComponentContext
    ): Child
}
