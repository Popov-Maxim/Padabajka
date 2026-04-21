package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.NavigateComponentContext
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.deeplink.AppDeeplink
import com.padabajka.dating.feature.image.presentation.ImageCropScreenComponent
import com.padabajka.dating.feature.messenger.presentation.MessengerComponent
import com.padabajka.dating.feature.messenger.presentation.chat.ChatComponent
import com.padabajka.dating.feature.messenger.presentation.model.MatchItem
import com.padabajka.dating.feature.profile.presentation.ProfileScreenComponent
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreenComponent
import com.padabajka.dating.feature.profile.presentation.editor.model.ImageAddEvent
import com.padabajka.dating.feature.reaction.screen.presentation.LikesMeScreenComponent
import com.padabajka.dating.feature.subscription.presentation.SubscriptionScreenComponent
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
import com.padabajka.dating.settings.presentation.SettingsScopeNavigateComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class MainAuthScopeNavigateComponent(
    context: ComponentContext,
    private val userId: UserId
) : NavigateComponentContext<MainAuthScopeNavigateComponent.Configuration, MainAuthScopeNavigateComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.SwiperScreen
),
    KoinComponent {

    fun openSwiper() {
        navigate(Configuration.SwiperScreen)
    }

    fun openProfile() {
        navigate(Configuration.ProfileScreen)
    }

    fun openSettings() {
        navigate(Configuration.SettingScreen)
    }

    fun openMessenger() {
        navigate(Configuration.MessengerScreen)
    }

    fun openLikes() {
        navigate(Configuration.LikesMeScreen)
    }

    fun onDeeplink(deeplink: AppDeeplink) {
        when (deeplink) {
            is AppDeeplink.OpenChat -> {
                navigate(Configuration.ChatScreen(deeplink.chatId))
            }

            AppDeeplink.OpenLikes -> openLikes()
        }
    }

    @Suppress("LongMethod")
    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.SwiperScreen -> Child.SwiperScreen(
                component = SwiperScreenComponent(
                    context = context,
                    openSubscriptionScreen = { navigate(Configuration.SubscriptionScreen) },
                    reactToCardUseCaseFactory = { get() },
                    nextCardUseCaseFactory = { get() },
                    updateSearchPrefUseCase = get(),
                    searchPreferencesProvider = get(),
                    profileRepository = get(),
                    subscriptionRepository = get(),
                    returnLastCardUseCase = get(),
                )
            )

            Configuration.ProfileScreen -> Child.ProfileScreen(
                component = ProfileScreenComponent(
                    context = context,
                    openEditor = { navigate(Configuration.ProfileEditorScreen) },
                    openLikesMeScreen = { navigate(Configuration.LikesMeScreen) },
                    openSubscriptionScreen = { navigate(Configuration.SubscriptionScreen) },
                    profileRepository = get(),
                    subscriptionRepository = get()
                )
            )

            Configuration.ProfileEditorScreen -> Child.ProfileEditorScreen(
                component = ProfileEditorScreenComponent(
                    context = context,
                    navigateBack = ::navigateBack,
                    cropImage = { image, index ->
                        navigate(Configuration.ImageCropScreen(image, index))
                    },
                    profileRepository = get(),
                    saveUpdatedProfileUseCaseFactory = { get() },
                    getLocalImageUseCaseFactory = { get() },
                    findCitiesUseCase = get(),
                    findLanguageAssetsUseCase = get(),
                    findInterestAssetsUseCase = get(),
                )
            )

            Configuration.SettingScreen -> Child.SettingScreen(
                component = SettingsScopeNavigateComponent(
                    context = context,
                    navigateBack = ::navigateBack
                )
            )

            is Configuration.ChatScreen -> Child.ChatScreen(
                component = get {
                    parametersOf(
                        context,
                        configuration.chatId,
                        configuration.matchItem,
                        userId,
                        ::navigateBack
                    )
                }
            )

            Configuration.MessengerScreen -> Child.MessengerScreen(
                component = get {
                    parametersOf(
                        context,
                        { chatId: ChatId, matchItem: MatchItem ->
                            navigate(Configuration.ChatScreen(chatId, matchItem))
                        }
                    )
                }
            )

            Configuration.LikesMeScreen -> Child.LikesMeScreen(
                component = LikesMeScreenComponent(
                    context = context,
                    openSubscriptionScreen = { navigate(Configuration.SubscriptionScreen) },
                    reactionsToMeUseCase = get(),
                    reactionRepository = get(),
                    subscriptionRepository = get()
                )
            )

            Configuration.SubscriptionScreen -> Child.SubscriptionScreen(
                component = SubscriptionScreenComponent(
                    context = context,
                    navigateBack = ::navigateBack,
                    subscriptionRepository = get()
                )
            )

            is Configuration.ImageCropScreen -> Child.ImageCropScreen(
                component = ImageCropScreenComponent(
                    componentContext = context,
                    image = configuration.image,
                    navigateBack = ::navigateBack,
                    onImageCropped = { rect ->
                        navigateBackWithResult { child ->
                            if (child is Child.ProfileEditorScreen) {
                                val image = configuration.image.copy(rect = rect)

                                child.component.onEvent(ImageAddEvent(image, configuration.index))
                            }
                        }
                    }
                )
            )
        }
    }

    sealed interface Child {
        data class ProfileEditorScreen(val component: ProfileEditorScreenComponent) : Child
        data class SettingScreen(val component: SettingsScopeNavigateComponent) : Child
        data class SubscriptionScreen(val component: SubscriptionScreenComponent) : Child
        data class ImageCropScreen(val component: ImageCropScreenComponent) : Child

        data class SwiperScreen(val component: SwiperScreenComponent) : Child
        data class ProfileScreen(val component: ProfileScreenComponent) : Child
        data class ChatScreen(val component: ChatComponent) : Child
        data class MessengerScreen(val component: MessengerComponent) : Child
        data class LikesMeScreen(val component: LikesMeScreenComponent) : Child
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object SwiperScreen : Configuration

        @Serializable
        data object ProfileScreen : Configuration

        @Serializable
        data object ProfileEditorScreen : Configuration

        @Serializable
        data object SettingScreen : Configuration

        @Serializable
        data object MessengerScreen : Configuration

        @Serializable
        data class ChatScreen(
            val chatId: ChatId,
            val matchItem: MatchItem? = null
        ) : Configuration

        @Serializable
        data object LikesMeScreen : Configuration

        @Serializable
        data object SubscriptionScreen : Configuration

        @Serializable
        data class ImageCropScreen(val image: Image.Local, val index: Int) : Configuration
    }
}
