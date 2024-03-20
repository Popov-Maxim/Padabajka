package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.repository.api.AuthRepository
import com.fp.padabajka.core.repository.api.MockAuthRepository
import com.fp.padabajka.feature.auth.data.InvalidCredentialsAuthException
import com.fp.padabajka.feature.auth.data.UnexpectedAuthException
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

    private fun getMockerRepositoryAndUseCase(): MockerRepositoryAndUseCase {
        val mocker = Mocker()
        val repository = MockAuthRepository(mocker)
        val useCase = LogInWithEmailAndPasswordUseCase(repository)
        return MockerRepositoryAndUseCase(mocker, repository, useCase)
    }

    @Test
    fun positive() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()

        mocker.everySuspending { repository.login(isAny(), isAny()) } returns Unit

        useCase.invoke("valid@email.com", "Password")
    }

    @Test
    fun negative_invalidCredentials() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()

        mocker.everySuspending {
            repository.login(isAny(), isAny())
        } throws InvalidCredentialsAuthException

        assertThrows(InvalidCredentialsLogInException) {
            useCase.invoke("valid@email.com", "Password")
        }
    }

    @Test
    fun negative_unexpectedException() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()
        val exception = UnexpectedAuthException(Exception())

        mocker.everySuspending {
            repository.login(isAny(), isAny())
        } throws exception

        assertThrows(UnexpectedLoginException(exception)) {
            useCase.invoke("valid@email.com", "Password")
        }
    }
}
