package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.NavigateComponentContext
import com.padabajka.dating.core.presentation.asFlow
import com.padabajka.dating.core.presentation.deeplink.AppDeeplinkHandler
import com.padabajka.dating.core.repository.api.metadata.PushRepository
import com.padabajka.dating.core.repository.api.model.auth.LoggedIn
import com.padabajka.dating.core.repository.api.model.auth.LoggedOut
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.auth.WaitingForEmailValidation
import com.padabajka.dating.core.sync.SyncSessionObserver
import com.padabajka.dating.feature.auth.domain.AuthStateProvider
import com.padabajka.dating.feature.auth.presentation.VerificationComponent
import com.padabajka.dating.session.UserSessionManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.cancellation.CancellationException

class AuthStateObserverComponent(
    context: ComponentContext,
    private val pushRepository: PushRepository,
    private val deeplinkHandler: AppDeeplinkHandler,
    private val syncSessionObserver: SyncSessionObserver,
    private val userSessionManager: UserSessionManager,
) : NavigateComponentContext<AuthStateObserverComponent.Configuration, AuthStateObserverComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.SplashScreen
),
    KoinComponent {

    init {
        navigateScope.launch {
            deeplinkHandler.appDeeplinks.collect {
                val instance = childStack
                    .asFlow()
                    .map { it.active.instance }
                    .filterIsInstance<Child.AuthScope>()
                    .first()

                instance.component.onDeeplink(it)
            }
        }
    }

    private val authProvider: AuthStateProvider = get()

    @Suppress("TooGenericExceptionCaught")
    suspend fun subscribeToAuth() {
        authProvider.authState.distinctUntilChanged().collect { authState ->
            when (authState) {
                LoggedOut -> {
                    navigateNewStack(Configuration.SplashScreen)
                    syncSessionObserver.stop()
                    userSessionManager.clear()
                    try {
                        pushRepository.deleteToken()
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (_: Throwable) {
                        // Local user data is already cleared; token recreation is retried on login.
                    }
                    navigateNewStack(Configuration.UnauthScope)
                }

                is LoggedIn -> {
                    navigateNewStack(Configuration.SplashScreen)
                    syncSessionObserver.stop()
                    userSessionManager.prepare(authState.userId)
                    navigateNewStack(Configuration.AuthScope(authState.userId))
                }

                is WaitingForEmailValidation -> {
                    navigateNewStack(Configuration.SplashScreen)
                    syncSessionObserver.stop()
                    userSessionManager.prepare(authState.userId)
                    navigateNewStack(Configuration.VerificationScreen)
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
                component = AuthScopeNavigateComponent(
                    context = context,
                    userId = configuration.userId,
                    updateAuthMetadataUseCase = get(),
                    profileRepository = get(),
                    domainErrorHandler = get(),
                    syncSessionObserver = get(),
                    legalRepository = get()
                )
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
