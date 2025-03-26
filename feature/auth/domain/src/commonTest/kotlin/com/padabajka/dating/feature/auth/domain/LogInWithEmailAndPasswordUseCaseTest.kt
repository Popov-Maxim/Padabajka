package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.MockAuthRepository
import com.padabajka.dating.core.repository.api.model.auth.InvalidCredentialsAuthException
import com.padabajka.dating.core.repository.api.model.auth.UnexpectedAuthException
import com.padabajka.dating.testing.assertThrows
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.Test

@UsesMocks(AuthRepository::class)
class LogInWithEmailAndPasswordUseCaseTest {

    private data class MockerRepositoryAndUseCase(
        val mocker: Mocker,
        val repository: AuthRepository,
        val useCase: LogInWithEmailAndPasswordUseCase
    )

    private val email = "valid@email.com"
    private val password = "Password"

    private fun getMockerRepositoryAndUseCase(): MockerRepositoryAndUseCase {
        val mocker = Mocker()
        val repository = MockAuthRepository(mocker)
        val useCase = LogInWithEmailAndPasswordUseCase(repository)
        return MockerRepositoryAndUseCase(mocker, repository, useCase)
    }

    @Test
    fun positive() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()

        mocker.everySuspending { repository.login(email, password) } returns Unit

        useCase.invoke(email, password)

        mocker.verifyWithSuspend { repository.login(email, password) }
    }

    @Test
    fun negative_invalidCredentials() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()

        mocker.everySuspending {
            repository.login(email, password)
        } throws InvalidCredentialsAuthException

        assertThrows(InvalidCredentialsLogInException) {
            useCase.invoke(email, password)
        }

        mocker.verifyWithSuspend { threw { repository.login(email, password) } }
    }

    @Test
    fun negative_unexpectedException() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()
        val exception = UnexpectedAuthException(Exception())

        mocker.everySuspending {
            repository.login(email, password)
        } throws exception

        assertThrows(UnexpectedLoginException(exception)) {
            useCase.invoke(email, password)
        }

        mocker.verifyWithSuspend { threw { repository.login(email, password) } }
    }
}
