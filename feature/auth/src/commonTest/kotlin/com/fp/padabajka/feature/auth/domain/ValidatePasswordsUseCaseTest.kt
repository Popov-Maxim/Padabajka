package com.fp.padabajka.feature.auth.domain

import kotlin.test.Test

class ValidatePasswordsUseCaseTest {

    private val useCase = ValidatePasswordsUseCase()

    @Test
    fun positive() = useCase("PASSwor1", "PASSwor1")

    @Test
    fun negative_blankPassword() =
        assertNegativeValidation(password = "  ", exception = PasswordIsBlank)

    @Test
    fun negative_passwordWithSpace() =
        assertNegativeValidation(password = "pass word", exception = PasswordHasWhitespaces)

    @Test
    fun negative_passwordToShort() =
        assertNegativeValidation(password = "1234567", exception = PasswordIsTooShort)

    @Test
    fun negative_passwordWithNoLowercaseLetters() =
        assertNegativeValidation(
            password = "12345678",
            exception = PasswordHasNoLowerCaseCharacters
        )

    @Test
    fun negative_passwordWithNoUppercaseLetters() =
        assertNegativeValidation(
            password = "a2345678",
            exception = PasswordHasNoUpperCaseCharacters
        )

    @Test
    fun negative_passwordWithNoDigits() =
        assertNegativeValidation(
            password = "aaaaaAAAA",
            exception = PasswordHasNoDigits
        )

    private fun assertNegativeValidation(
        password: String,
        password2: String = "",
        exception: Throwable
    ) = assertThrows(exception) {
        useCase(password, password2)
    }
}
