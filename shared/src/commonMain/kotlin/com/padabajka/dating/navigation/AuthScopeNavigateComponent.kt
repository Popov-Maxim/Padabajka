package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.profile.ProfileState
import com.padabajka.dating.domain.SyncRemoteDataUseCase
import com.padabajka.dating.feature.push.socket.domain.SocketMessageObserver
import com.padabajka.dating.settings.domain.NewAuthMetadataUseCase
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

class AuthScopeNavigateComponent(
    context: ComponentContext,
    private val userId: UserId,
    private val updateAuthMetadataUseCase: NewAuthMetadataUseCase,
    private val syncRemoteDataUseCase: SyncRemoteDataUseCase,
    private val socketMessageObserver: SocketMessageObserver,
    private val profileRepository: ProfileRepository
) : NavigateComponentContext<AuthScopeNavigateComponent.Configuration, AuthScopeNavigateComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.LoginScreen
),
    KoinComponent {

    init {
        backgroundScope.launch {
            profileRepository.profileState.collect { profileState ->
                when (profileState) {
                    ProfileState.Idle -> Unit
                    is ProfileState.Existing -> {
                        updateAuthMetadataUseCase()
                        socketMessageObserver.subscribe()
                        syncRemoteDataUseCase()

                        navigateNewStack(Configuration.MainAuthScope)
                    }

                    ProfileState.NotCreated -> navigateNewStack(Configuration.CreateProfileScope)
                }
            }
        }
        backgroundScope.launch {
            profileRepository.updateProfile()
        }
    }

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.LoginScreen -> Child.LoginScreen
            is Configuration.CreateProfileScope -> Child.CreateProfileScope(
                component = CreateProfileScopeNavigateComponent(context)
            )

            is Configuration.MainAuthScope -> Child.MainAuthScope(
                component = MainAuthScopeNavigateComponent(context, userId)
            )
        }
    }

    sealed interface Child {
        data object LoginScreen : Child
        data class CreateProfileScope(val component: CreateProfileScopeNavigateComponent) : Child
        data class MainAuthScope(val component: MainAuthScopeNavigateComponent) : Child
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object LoginScreen : Configuration

        @Serializable
        data object CreateProfileScope : Configuration

        @Serializable
        data object MainAuthScope : Configuration
    }
}
