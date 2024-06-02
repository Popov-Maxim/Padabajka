package com.fp.padabajka

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.fp.padabajka.core.repository.api.model.messenger.ChatId
import com.fp.padabajka.feature.messenger.presentation.MessengerComponent
import com.fp.padabajka.feature.swiper.presentation.SwiperScreenComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class NavigateComponentContext(
    context: ComponentContext
) : ComponentContext by context, KoinComponent {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.MessengerScreen(ChatId("fakeChatId")),
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    fun navigate(configuration: Configuration) {
        navigation.pushNew(configuration)
    }

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.SwiperScreen -> Child.SwiperScreen(
                component = get { parametersOf(context) }
            )

            is Configuration.MessengerScreen -> Child.MessengerScreen(
                component = get { parametersOf(context, configuration.chatId) }
            )
        }
    }

    sealed interface Child {
        data class SwiperScreen(val component: SwiperScreenComponent) : Child
        data class MessengerScreen(val component: MessengerComponent) : Child
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object SwiperScreen : Configuration

        @Serializable
        data class MessengerScreen(val chatId: ChatId) : Configuration
    }
}
