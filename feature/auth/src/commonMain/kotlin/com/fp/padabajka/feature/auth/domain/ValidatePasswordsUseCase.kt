package com.fp.padabajka.feature.auth.domain

class ValidatePasswordsUseCase {

    @Throws(
        PasswordIsBlank::class,
        PasswordHasWhitespaces::class,
        PasswordIsTooShort::class,
        PasswordHasNoLowerCaseCharacters::class,
        PasswordHasNoUpperCaseCharacters::class,
        PasswordHasNoDigits::class,
        PasswordsNotMatching::class
    )
    operator fun invoke(password: String, password2: String) {
        when {
            password.isBlank() -> throw PasswordIsBlank
            password.any { it.isWhitespace() } -> throw PasswordHasWhitespaces
            password.length < PASSWORD_MIN_LENGTH -> throw PasswordIsTooShort
            password.none { it.isLetter() && it.isLowerCase() } -> throw PasswordHasNoLowerCaseCharacters
            password.none { it.isLetter() && it.isUpperCase() } -> throw PasswordHasNoUpperCaseCharacters
            password.none { it.isDigit() } -> throw PasswordHasNoDigits
            password != password2 -> throw PasswordsNotMatching
        }
    }

    companion object {
        const val PASSWORD_MIN_LENGTH = 8
    }
}

sealed class PasswordsValidationException(message: String) : Throwable(message)

data object PasswordIsBlank : PasswordsValidationException("Password is blank!")
data object PasswordHasWhitespaces : PasswordsValidationException("Password has whitespaces!")

data object PasswordIsTooShort : PasswordsValidationException("Password is too short!")
data object PasswordHasNoLowerCaseCharacters :
    PasswordsValidationException("Password has no lower case characters!")

data object PasswordHasNoUpperCaseCharacters :
    PasswordsValidationException("Password has no lower case characters!")

data object PasswordHasNoDigits : PasswordsValidationException("Password has no digits!")
data object PasswordsNotMatching : PasswordsValidationException("Passwords not matching!")
