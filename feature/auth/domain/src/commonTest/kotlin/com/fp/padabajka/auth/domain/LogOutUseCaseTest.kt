package com.fp.padabajka.auth.domain

import com.fp.padabajka.core.repository.api.AuthRepository
import com.fp.padabajka.core.repository.api.MockAuthRepository
import com.fp.padabajka.core.repository.api.model.auth.UnexpectedAuthException
import com.fp.padabajka.testing.assertThrows
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.Test

@UsesMocks(AuthRepository::class)
class LogOutUseCaseTest {

    private data class MockerRepositoryAndUseCase(
        val mocker: Mocker,
        val repository: AuthRepository,
        val useCase: LogOutUseCase
    )

    private fun getMockerRepositoryAndUseCase(): MockerRepositoryAndUseCase {
        val mocker = Mocker()
        val repository = MockAuthRepository(mocker)
        val useCase = LogOutUseCase(repository)
        return MockerRepositoryAndUseCase(mocker, repository, useCase)
    }

    @Test
    fun positive() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()

        mocker.everySuspending { repository.logout() } returns Unit

        useCase.invoke()

        mocker.verifyWithSuspend { repository.logout() }
    }

    @Test
    fun negative_unexpectedException() = runBlocking {
        val (mocker, repository, useCase) = getMockerRepositoryAndUseCase()
        val exception = UnexpectedAuthException(Exception())

        mocker.everySuspending { repository.logout() } throws exception

        assertThrows(UnexpectedLogoutException(exception)) {
            useCase.invoke()
        }

        mocker.verifyWithSuspend { threw { repository.logout() } }
    }
}
