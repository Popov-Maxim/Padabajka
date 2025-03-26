package com.padabajka.dating.feature.auth.presentation.model

enum class PasswordValidationIssue {
    PasswordIsBlank,
    PasswordHasWhitespaces,
    PasswordIsTooShort,
    PasswordHasNoLowerCaseCharacters,
    PasswordHasNoUpperCaseCharacters,
    PasswordHasNoDigits,
    PasswordsNotMatching,
    UnableToValidatePassword
}
