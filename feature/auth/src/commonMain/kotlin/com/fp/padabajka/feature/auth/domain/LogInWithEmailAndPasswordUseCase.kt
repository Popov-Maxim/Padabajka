package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.repository.api.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

// TODO Add tests when I got implemented
class LogInWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>
) {

    @Suppress("TooGenericExceptionCaught")
    @Throws(
        InvalidCredentialsLogInException::class,
        UnexpectedLoginException::class,
        CancellationException::class
    )
    suspend operator fun invoke(email: String, password: String) {
        try {
            validateEmailUseCase.get().invoke(email)

            authRepository.login(email, password)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            throw when (e) {
                is EmailValidationException -> e
                // TODO map other exceptions
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
