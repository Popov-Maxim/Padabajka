package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.feature.auth.domain.ReloadUserUseCase
import com.padabajka.dating.feature.auth.domain.SendEmailVerificationUseCase
import com.padabajka.dating.feature.auth.presentation.model.ContinueVerification
import com.padabajka.dating.feature.auth.presentation.model.ResendState
import com.padabajka.dating.feature.auth.presentation.model.ResendVerification
import com.padabajka.dating.feature.auth.presentation.model.VerificationEvent
import com.padabajka.dating.feature.auth.presentation.model.VerificationState
import kotlinx.coroutines.delay

class VerificationComponent(
    context: ComponentContext,
    sendEmailVerificationUseCaseFactory: Factory<SendEmailVerificationUseCase>,
    reloadUserUseCaseFactory: Factory<ReloadUserUseCase>
) : BaseComponent<VerificationState>(context, VerificationState(ResendState.Available)) {

    private val sendEmailVerificationUseCase by sendEmailVerificationUseCaseFactory.delegate()
    private val reloadUserUseCase by reloadUserUseCaseFactory.delegate()

    fun onEvent(event: VerificationEvent) {
        when (event) {
            ContinueVerification -> continueVerification()
            ResendVerification -> resendVerification()
        }
    }

    private fun continueVerification() = mapAndReduceException(
        action = {
            reloadUserUseCase()
        },
        mapper = { TODO(it.toString()) },
        update = { state, _ ->
            state
        }
    )

    private fun resendVerification() = mapAndReduceException(
        action = {
            sendEmailVerificationUseCase()
            makeResendUnavailable()
        },
        mapper = { TODO(it.toString()) },
        update = { state, _ ->
            state
        }
    )

    @Suppress("MagicNumber")
    private suspend fun makeResendUnavailable() {
        for (i in 10 downTo 1) {
            reduce {
                it.copy(resendState = ResendState.Unavailable(i))
            }
            delay(1000)
        }
        reduce {
            it.copy(resendState = ResendState.Available)
        }
    }
}
