package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.repository.api.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

class LogOutUseCase(private val authRepository: AuthRepository) {

    @Suppress("TooGenericExceptionCaught")
    @Throws(UnexpectedLogoutException::class, CancellationException::class)
    suspend operator fun invoke() {
        try {
            authRepository.logout()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            throw UnexpectedLogoutException(e)
        }
    }
}

data class UnexpectedLogoutException(override val cause: Throwable) :
    Throwable("Exception when login out: $cause")
