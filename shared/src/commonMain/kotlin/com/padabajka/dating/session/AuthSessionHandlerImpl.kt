package com.padabajka.dating.session

import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.AuthSessionHandler
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.utils.runCatchingCancellable
import com.padabajka.dating.settings.domain.DeleteAuthMetadataUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

class AuthSessionHandlerImpl(
    private val authRepository: AuthRepository,
    private val alertService: AlertService,
    private val deleteAuthMetadataUseCase: DeleteAuthMetadataUseCase,
) : AuthSessionHandler {

    override suspend fun onUnauthorized() {
        val userId = authRepository.currentAuthState.userIdOrNull() ?: return

        runCatchingCancellable {
            deleteAuthMetadataUseCase()
        }

        if (authRepository.currentAuthState.userIdOrNull() != userId) return

        runCatchingCancellable {
            authRepository.logout()
        }.onFailure { exception ->
            alertService.showAlert { StaticTextId.UiId.UnknownErrorDescription.translate() }
            Firebase.crashlytics.recordException(exception)
        }
    }
}
