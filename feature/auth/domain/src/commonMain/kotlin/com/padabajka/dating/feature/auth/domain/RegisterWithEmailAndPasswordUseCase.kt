package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.repository.api.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

// TODO Add tests when I got implemented
class RegisterWithEmailAndPasswordUseCase(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>,
    private val validatePasswordsUseCase: Factory<ValidatePasswordsUseCase>,
) {

    @Suppress("TooGenericExceptionCaught")
    suspend operator fun invoke(email: String, password: String, repeatedPassword: String) {
        try {
            validateEmailUseCase.get().invoke(email)
            validatePasswordsUseCase.get().invoke(password, repeatedPassword)

            authRepository.register(email, password)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: Throwable) {
            throw when (e) {
                is EmailValidationException,
                is PasswordsValidationException -> InvalidCredentialsRegisteringException
                // TODO map other exceptions
                else -> UnexpectedRegisterException(e)
            }
        }
    }
}

sealed class RegisteringWithEmailAndPasswordException(message: String) : Throwable(message)

data object InvalidCredentialsRegisteringException :
    RegisteringWithEmailAndPasswordException("Invalid email or password!")

// TODO Add other exceptions
data class UnexpectedRegisterException(override val cause: Throwable) :
    RegisteringWithEmailAndPasswordException("Unexpected $cause!")
