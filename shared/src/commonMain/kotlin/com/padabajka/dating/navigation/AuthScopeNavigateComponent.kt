package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.NavigateComponentContext
import com.padabajka.dating.core.presentation.asFlow
import com.padabajka.dating.core.presentation.error.DomainErrorHandler
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.metadata.LegalRepository
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.deeplink.AppDeeplink
import com.padabajka.dating.core.repository.api.model.legal.LegalState
import com.padabajka.dating.core.repository.api.model.profile.ProfileState
import com.padabajka.dating.core.sync.SyncSessionObserver
import com.padabajka.dating.feature.auth.presentation.AccountDeletedScreenComponent
import com.padabajka.dating.feature.legal.presentation.NewLegalAgreementsComponent
import com.padabajka.dating.navigation.AuthScopeNavigateComponent.Child.CreateProfileScope
import com.padabajka.dating.navigation.AuthScopeNavigateComponent.Child.LoadingErrorScreen
import com.padabajka.dating.navigation.AuthScopeNavigateComponent.Child.LoadingProfileScreen
import com.padabajka.dating.navigation.AuthScopeNavigateComponent.Child.MainAuthScope
import com.padabajka.dating.navigation.AuthScopeNavigateComponent.Child.NewLegalAgreementsScreen
import com.padabajka.dating.navigation.AuthScopeNavigateComponent.Child.UserDeletedScreen
import com.padabajka.dating.settings.domain.NewAuthMetadataUseCase
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AuthScopeNavigateComponent(
    context: ComponentContext,
    private val userId: UserId,
    private val updateAuthMetadataUseCase: NewAuthMetadataUseCase,
    private val profileRepository: ProfileRepository,
    private val legalRepository: LegalRepository,
    private val syncSessionObserver: SyncSessionObserver,
    private val domainErrorHandler: DomainErrorHandler,
) : NavigateComponentContext<AuthScopeNavigateComponent.Configuration, AuthScopeNavigateComponent.Child>(
    context,
    Configuration.serializer(),
    Configuration.LoadingProfileScreen
),
    KoinComponent {

    init {
        backgroundScope.launch {
            legalRepository.userLegalState.collect { legalState ->
                when (legalState) {
                    LegalState.Idle -> Unit
                    LegalState.AllAccepted -> {
                        finishPrepare()
                    }

                    is LegalState.NeedAccent -> navigateNewStack(
                        Configuration.NewLegalAgreements(legalState.toData())
                    )
                }
            }
        }
        backgroundScope.launch {
            profileRepository.profileState.collect { profileState ->
                when (profileState) {
                    ProfileState.Idle -> Unit
                    ProfileState.NotCreated -> navigateNewStack(Configuration.CreateProfileScope)
                    is ProfileState.Existing -> {
                        updateLegal()
                    }
                }
            }
        }
        updateProfile()
    }

    private suspend fun finishPrepare() {
        updateAuthMetadataUseCase()
        syncSessionObserver.start()

        navigateNewStack(Configuration.MainAuthScope)
    }

    override fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ): Child {
        return when (configuration) {
            Configuration.LoadingProfileScreen -> LoadingProfileScreen
            is Configuration.CreateProfileScope -> CreateProfileScope(
                component = CreateProfileScopeNavigateComponent(context)
            )

            is Configuration.MainAuthScope -> MainAuthScope(
                component = MainAuthScopeNavigateComponent(context, userId)
            )

            is Configuration.LoadingErrorScreen -> LoadingErrorScreen(
                messageId = configuration.messageId,
                message = configuration.message,
                retry = {
                    navigateNewStack(Configuration.LoadingProfileScreen)
                    if (configuration.needUpdateProfile) {
                        updateProfile()
                    } else {
                        updateLegal()
                    }
                }
            )

            is Configuration.UserDeletedScreen -> UserDeletedScreen(
                messageId = configuration.messageId,
                component = AccountDeletedScreenComponent(
                    context = context,
                    logoutUseCase = get(),
                    alertService = get()
                )
            )

            is Configuration.NewLegalAgreements -> NewLegalAgreementsScreen(
                component = NewLegalAgreementsComponent(
                    context = context,
                    legalData = configuration.legalData,
                    logoutUseCase = get(),
                    alertService = get(),
                    legalRepository = legalRepository
                )
            )
        }
    }

    suspend fun onDeeplink(deeplink: AppDeeplink) {
        if (deeplink is AppDeeplink.OpenUserDeleteScreen) {
            val textId = if (deeplink.banned) {
                StaticTextId.UiId.AccountBannedDescription
            } else {
                StaticTextId.UiId.AccountDeletedDescription
            }
            navigateNewStack(Configuration.UserDeletedScreen(textId))
        } else {
            val instance = childStack
                .asFlow()
                .map { it.active.instance }
                .filterIsInstance<MainAuthScope>()
                .first()

            instance.component.onDeeplink(deeplink)
        }
    }

    private fun updateProfile() {
        backgroundScope.launch {
            runCatching {
                profileRepository.updateProfile()
            }.onFailure { throwable ->
                domainErrorHandler.handle(throwable) { error ->
                    val error = when (error) {
                        is ExternalDomainError.TextError -> error
                        is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
                    }

                    navigateNewStack(
                        Configuration.LoadingErrorScreen(
                            error.text,
                            throwable.message ?: throwable.toString(),
                            true
                        )
                    )
                    error.needLog.not()
                }
            }
        }
    }

    private fun updateLegal() {
        backgroundScope.launch {
            runCatching {
                legalRepository.updateUserLegalState()
            }.onFailure { throwable ->
                domainErrorHandler.handle(throwable) { error ->
                    val error = when (error) {
                        is ExternalDomainError.TextError -> error
                        is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
                    }

                    navigateNewStack(
                        Configuration.LoadingErrorScreen(
                            error.text,
                            throwable.message ?: throwable.toString(),
                            false
                        )
                    )
                    error.needLog.not()
                }
            }
        }
    }

    private fun LegalState.NeedAccent.toData(): NewLegalAgreementsComponent.Data {
        return NewLegalAgreementsComponent.Data(
            privacy = privacy,
            terms = terms
        )
    }

    sealed interface Child {
        data object LoadingProfileScreen : Child
        data class LoadingErrorScreen(
            val messageId: StaticTextId,
            val message: String,
            val retry: () -> Unit
        ) : Child

        data class CreateProfileScope(val component: CreateProfileScopeNavigateComponent) : Child
        data class UserDeletedScreen(
            val messageId: StaticTextId,
            val component: AccountDeletedScreenComponent
        ) : Child

        data class NewLegalAgreementsScreen(
            val component: NewLegalAgreementsComponent
        ) : Child

        data class MainAuthScope(val component: MainAuthScopeNavigateComponent) : Child
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object LoadingProfileScreen : Configuration

        @Serializable
        data class LoadingErrorScreen(
            val messageId: StaticTextId,
            val message: String,
            val needUpdateProfile: Boolean
        ) : Configuration

        @Serializable
        data object CreateProfileScope : Configuration

        @Serializable
        data class UserDeletedScreen(val messageId: StaticTextId) : Configuration

        @Serializable
        data class NewLegalAgreements(val legalData: NewLegalAgreementsComponent.Data) : Configuration

        @Serializable
        data object MainAuthScope : Configuration
    }
}
