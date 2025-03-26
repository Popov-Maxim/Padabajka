package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.testing.assertThrows
import kotlin.test.Test

class ValidatePasswordsUseCaseTest {

    private val useCase = ValidatePasswordsUseCase()

    @Test
    fun positive() = useCase("PASSwor1", "PASSwor1")

    @Test
    fun negative_blankPassword() =
        assertNegativeValidation(password = "  ", exception = PasswordIsBlankException)

    @Test
    fun negative_passwordWithSpace() =
        assertNegativeValidation(password = "pass word", exception = PasswordHasWhitespacesException)

    @Test
    fun negative_passwordToShort() =
        assertNegativeValidation(password = "1234567", exception = PasswordIsTooShortException)

    @Test
    fun negative_passwordWithNoLowercaseLetters() =
        assertNegativeValidation(
            password = "12345678",
            exception = PasswordHasNoLowerCaseCharactersException
        )

    @Test
    fun negative_passwordWithNoUppercaseLetters() =
        assertNegativeValidation(
            password = "a2345678",
            exception = PasswordHasNoUpperCaseCharactersException
        )

    @Test
    fun negative_passwordWithNoDigits() =
        assertNegativeValidation(
            password = "aaaaaAAAA",
            exception = PasswordHasNoDigitsException
        )

    private fun assertNegativeValidation(
        password: String,
        repeatedPassword: String = "",
        exception: Throwable
    ) = assertThrows(exception) {
        useCase(password, repeatedPassword)
    }
}
