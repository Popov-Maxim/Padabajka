package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import kotlinx.serialization.KSerializer
import org.koin.core.component.KoinComponent

abstract class NavigateComponentContext<Config : Any, Child : Any> (
    context: ComponentContext,
    serializable: KSerializer<Config>,
    initialConfiguration: Config,
    private val splash: Config? = null
) : ComponentContext by context, KoinComponent {

    private val navigation = StackNavigation<Config>()
    val childStack = childStack(
        source = navigation,
        serializer = serializable,
        initialConfiguration = initialConfiguration,
        handleBackButton = true,
        childFactory = ::createChild
    )

    protected fun navigate(configuration: Config) {
        navigation.navigate { stack ->
            (splash?.let { stack - it } ?: stack) - configuration + configuration
        }
    }

    protected fun navigateNewStack(configuration: Config) {
        navigation.navigate {
            listOf(configuration)
        }
    }

    protected fun navigateBack() {
        navigation.pop()
    }

    protected abstract fun createChild(
        configuration: Config,
        context: ComponentContext
    ): Child
}
