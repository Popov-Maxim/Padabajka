package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.repository.api.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

class LogInWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>,
    private val validatePasswordsUseCase: Factory<ValidatePasswordsUseCase>,
) {

    @Throws(
        InvalidCredentialsException::class,
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
            when (e) {
                is EmailValidationException,
                is PasswordsValidationException -> throw InvalidCredentialsException
                // TODO map other exceptions
                else -> throw UnexpectedLoginException(e)
            }
        }
    }
}

sealed class LogInWithEmailAndPasswordException(message: String) : Throwable(message)

data object InvalidCredentialsException :
    LogInWithEmailAndPasswordException("Invalid email or password!")

// TODO Add other exceptions
data class UnexpectedLoginException(override val cause: Throwable) :
    LogInWithEmailAndPasswordException("Unexpected $cause!")
