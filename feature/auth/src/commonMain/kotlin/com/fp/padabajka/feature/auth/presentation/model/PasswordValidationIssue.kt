package com.fp.padabajka.feature.auth.presentation.model

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
