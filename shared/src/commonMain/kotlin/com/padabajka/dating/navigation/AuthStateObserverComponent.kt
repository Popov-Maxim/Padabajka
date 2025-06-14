package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.repository.api.model.auth.LoggedIn
import com.padabajka.dating.core.repository.api.model.auth.LoggedOut
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.auth.WaitingForEmailValidation
import com.padabajka.dating.domain.SyncRemoteDataUseCase
import com.padabajka.dating.feature.auth.domain.AuthStateProvider
import com.padabajka.dating.feature.auth.presentation.VerificationComponent
import com.padabajka.dating.feature.push.socket.domain.SocketMessageObserver
import com.padabajka.dating.settings.domain.NewAuthMetadataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class AuthStateObserverComponent(
    context: ComponentContext,
    private val updateAuthMetadataUseCase: NewAuthMetadataUseCase,
    private val syncRemoteDataUseCase: SyncRemoteDataUseCase,
    private val socketMessageObserver: SocketMessageObserver,
) : NavigateComponentContext<AuthStateObserverComponent.Configuration, AuthStateObserverComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.SplashScreen
),
    KoinComponent {

    private val authProvider: AuthStateProvider = get()
    private val componentScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default) // TODO

    suspend fun subscribeToAuth() {
        authProvider.authState.collect { authState ->
            when (authState) {
                LoggedOut -> {
                    navigateNewStack(Configuration.UnauthScope)
                    componentScope.launch {
                        socketMessageObserver.unsubscribe()
                    }
                }
                is LoggedIn -> {
                    navigateNewStack(Configuration.AuthScope(authState.userId))
                    componentScope.launch {
                        updateAuthMetadataUseCase()
                        socketMessageObserver.subscribe()
                        syncRemoteDataUseCase()
                    }
                }

                is WaitingForEmailValidation -> {
                    navigateNewStack(Configuration.VerificationScreen)
                    componentScope.launch {
                        updateAuthMetadataUseCase()
                        socketMessageObserver.subscribe()
                    }
                }
            }
        }
    }

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.SplashScreen -> Child.SplashScreen
            Configuration.UnauthScope -> Child.UnauthScope(
                component = UnauthScopeNavigateComponent(context)
            )

            is Configuration.AuthScope -> Child.AuthScope(
                component = AuthScopeNavigateComponent(context, configuration.userId)
            )

            Configuration.VerificationScreen -> Child.VerificationScreen(
                component = get {
                    parametersOf(context)
                }
            )
        }
    }

    sealed interface Child {
        data object SplashScreen : Child
        data class UnauthScope(val component: UnauthScopeNavigateComponent) : Child
        data class VerificationScreen(val component: VerificationComponent) : Child
        data class AuthScope(val component: AuthScopeNavigateComponent) : Child
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        data object SplashScreen : Configuration

        @Serializable
        data object UnauthScope : Configuration

        @Serializable
        data object VerificationScreen : Configuration

        @Serializable
        data class AuthScope(val userId: UserId) : Configuration
    }
}
