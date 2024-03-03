package com.fp.padabajka.feature.auth.domain

class ValidateEmailUseCase {

    @Throws(EmailIsBlankException::class, InvalidEmailException::class)
    operator fun invoke(email: String) {
        when {
            email.isBlank() -> throw EmailIsBlankException
            email.matches(regex).not() -> throw InvalidEmailException
        }
    }

    companion object {
        private val regex = (
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})\$"
                ).toRegex()
    }
}

sealed class EmailValidationException(message: String) : Throwable(message)

data object EmailIsBlankException : EmailValidationException("Email is blank!")
data object InvalidEmailException : EmailValidationException("Invalid email!")
