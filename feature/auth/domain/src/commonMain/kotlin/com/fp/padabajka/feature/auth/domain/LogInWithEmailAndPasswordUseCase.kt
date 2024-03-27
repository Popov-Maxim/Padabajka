package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.repository.api.AuthRepository
import com.fp.padabajka.core.repository.api.model.auth.InvalidCredentialsAuthException
import kotlin.coroutines.cancellation.CancellationException

// TODO Add tests when I got implemented
class LogInWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository
) {

    @Suppress("TooGenericExceptionCaught")
    @Throws(
        LogInWithEmailAndPasswordException::class,
        CancellationException::class
    )
    suspend operator fun invoke(email: String, password: String) {
        try {
            authRepository.login(email, password)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            throw when (e) {
                is InvalidCredentialsAuthException -> InvalidCredentialsLogInException
                else -> UnexpectedLoginException(e)
            }
        }
    }
}

sealed class LogInWithEmailAndPasswordException(message: String) : Throwable(message)

data object InvalidCredentialsLogInException :
    LogInWithEmailAndPasswordException("Invalid email or password!")

// TODO Add other exceptions
data class UnexpectedLoginException(override val cause: Throwable) :
    LogInWithEmailAndPasswordException("Unexpected $cause!")
