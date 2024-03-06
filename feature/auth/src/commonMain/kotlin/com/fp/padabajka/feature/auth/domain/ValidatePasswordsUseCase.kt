package com.fp.padabajka.feature.auth.domain

class ValidatePasswordsUseCase {

    @Throws(
        PasswordIsBlankException::class,
        PasswordHasWhitespacesException::class,
        PasswordIsTooShortException::class,
        PasswordHasNoLowerCaseCharactersException::class,
        PasswordHasNoUpperCaseCharactersException::class,
        PasswordHasNoDigitsException::class,
        PasswordsNotMatchingException::class
    )
    operator fun invoke(password: String, repeatedPassword: String) {
        when {
            password.isBlank() -> throw PasswordIsBlankException
            password.any { it.isWhitespace() } -> throw PasswordHasWhitespacesException
            password.length < PASSWORD_MIN_LENGTH -> throw PasswordIsTooShortException
            password.none { it.isLetter() && it.isLowerCase() } -> throw PasswordHasNoLowerCaseCharactersException
            password.none { it.isLetter() && it.isUpperCase() } -> throw PasswordHasNoUpperCaseCharactersException
            password.none { it.isDigit() } -> throw PasswordHasNoDigitsException
            password != repeatedPassword -> throw PasswordsNotMatchingException
        }
    }

    companion object {
        const val PASSWORD_MIN_LENGTH = 8
    }
}

sealed class PasswordsValidationException(message: String) : Throwable(message)

data object PasswordIsBlankException : PasswordsValidationException("Password is blank!")
data object PasswordHasWhitespacesException : PasswordsValidationException("Password has whitespaces!")

data object PasswordIsTooShortException : PasswordsValidationException("Password is too short!")
data object PasswordHasNoLowerCaseCharactersException :
    PasswordsValidationException("Password has no lower case characters!")

data object PasswordHasNoUpperCaseCharactersException :
    PasswordsValidationException("Password has no lower case characters!")

data object PasswordHasNoDigitsException : PasswordsValidationException("Password has no digits!")
data object PasswordsNotMatchingException : PasswordsValidationException("Passwords not matching!")
