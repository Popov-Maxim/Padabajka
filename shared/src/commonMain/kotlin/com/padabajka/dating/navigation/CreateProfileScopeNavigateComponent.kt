package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.feature.profile.presentation.creator.birthday.CreateProfileBirthdayScreenComponent
import com.padabajka.dating.feature.profile.presentation.creator.gender.CreateProfileSexScreenComponent
import com.padabajka.dating.feature.profile.presentation.creator.lookingfor.CreateProfileLookingForScreenComponent
import com.padabajka.dating.feature.profile.presentation.creator.name.CreateProfileNameScreenComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class CreateProfileScopeNavigateComponent(
    context: ComponentContext,
) : NavigateComponentContext<
    CreateProfileScopeNavigateComponent.Configuration,
    CreateProfileScopeNavigateComponent.Child
    >(
    context,
    Configuration.serializer(),
    Configuration.WelcomeScreen
),
    KoinComponent {

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.WelcomeScreen -> Child.WelcomeScreen(
                toNext = { navigate(Configuration.NameScreen) }
            )

            Configuration.NameScreen -> Child.NameScreen(
                component = CreateProfileNameScreenComponent(
                    context = context,
                    nameValidator = get(),
                    saveNameUseCase = get(),
                    draftProfileProvider = get(),
                    toNext = { navigate(Configuration.BirthdayScreen) }
                )
            )

            Configuration.BirthdayScreen -> Child.BirthdayScreen(
                component = CreateProfileBirthdayScreenComponent(
                    context = context,
                    birthdayUpdateUseCase = get(),
                    birthdayValidator = get(),
                    draftProfileProvider = get(),
                    toNext = { navigate(Configuration.SexAndPreferencesScreen) },
                )
            )
            Configuration.SexAndPreferencesScreen -> Child.SexAndPreferencesScreen(
                component = CreateProfileSexScreenComponent(
                    context = context,
                    draftProfileProvider = get(),
                    updateUserGenderUseCase = get(),
                    searchPreferencesRepository = get(),
                    toNext = { navigate(Configuration.TypeLookingForScreen) },
                )
            )
            Configuration.TypeLookingForScreen -> Child.TypeLookingForScreen(
                onTypeSelected = { type ->
                    navigate(Configuration.DetailLookingForScreen(type))
                }
            )
            is Configuration.DetailLookingForScreen -> Child.DetailLookingForScreen(
                component = CreateProfileLookingForScreenComponent(
                    context = context,
                    selectedType = configuration.type,
                    updateLookingForUseCase = get(),
                    toNext = { navigate(Configuration.ImageScreen) }
                )
            )
            Configuration.ImageScreen -> Child.ImageScreen
        }
    }

    sealed interface Child {
        data class WelcomeScreen(val toNext: () -> Unit) : Child

        data class NameScreen(val component: CreateProfileNameScreenComponent) : Child

        data class BirthdayScreen(val component: CreateProfileBirthdayScreenComponent) : Child

        data class SexAndPreferencesScreen(val component: CreateProfileSexScreenComponent) : Child

        data class TypeLookingForScreen(val onTypeSelected: (StaticTextId) -> Unit) : Child

        data class DetailLookingForScreen(val component: CreateProfileLookingForScreenComponent) : Child

        data object ImageScreen : Child
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object WelcomeScreen : Configuration

        @Serializable
        data object NameScreen : Configuration

        @Serializable
        data object BirthdayScreen : Configuration

        @Serializable
        data object SexAndPreferencesScreen : Configuration

        @Serializable
        data object TypeLookingForScreen : Configuration

        @Serializable
        data class DetailLookingForScreen(val type: StaticTextId) : Configuration

        @Serializable
        data object ImageScreen : Configuration
    }
}
