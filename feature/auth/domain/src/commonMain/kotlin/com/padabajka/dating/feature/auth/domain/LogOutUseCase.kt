package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.exception.UserException
import com.padabajka.dating.settings.domain.DeleteAuthMetadataUseCase
import kotlin.coroutines.cancellation.CancellationException

class LogOutUseCase(
    private val authRepository: AuthRepository,
    private val deleteAuthMetadataUseCase: DeleteAuthMetadataUseCase
) {

    @Suppress("TooGenericExceptionCaught")
    @Throws(UnexpectedLogoutException::class, CancellationException::class)
    suspend operator fun invoke() {
        try {
            runCatching {
                deleteAuthMetadataUseCase.invoke()
            }.onFailure {
                if (it !is UserException.Banned) {
                    throw it
                }
            }
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
