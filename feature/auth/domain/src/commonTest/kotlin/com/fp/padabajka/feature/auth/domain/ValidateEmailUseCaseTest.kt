package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.testing.assertThrows
import kotlin.test.Test

class ValidateEmailUseCaseTest {

    private val useCase = ValidateEmailUseCase()

    @Test
    fun positive() = useCase("example@padabajka.com")

    @Test
    fun negative_blankEmail() = assertNegativeValidation("  ", EmailIsBlankException)

    @Test
    fun negative_invalidEmail() =
        listOf("email", "email@", "@email", "em@il", "email@em.co!,", "223@332.23")
            .forEach { assertNegativeValidation(it, InvalidEmailException) }

    private fun assertNegativeValidation(email: String, exception: Throwable) =
        assertThrows(exception) {
            useCase(email)
        }
}
