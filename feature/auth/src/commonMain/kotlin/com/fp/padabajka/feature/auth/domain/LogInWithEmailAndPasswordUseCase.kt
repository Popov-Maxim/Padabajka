package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.repository.api.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

// TODO Add tests when I got implemented
@Suppress("UnusedPrivateProperty")
class LogInWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>,
    private val validatePasswordsUseCase: Factory<ValidatePasswordsUseCase>,
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
            validatePasswordsUseCase.get().invoke(password, password)

            // TODO Implement login logic call
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            throw when (e) {
                is EmailValidationException,
                is PasswordsValidationException -> InvalidCredentialsLogInException
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