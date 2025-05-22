package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.MockAuthRepository
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.core.repository.api.metadata.MockMetadataRepository
import com.padabajka.dating.core.repository.api.model.auth.UnexpectedAuthException
import com.padabajka.dating.settings.domain.DeleteAuthMetadataUseCase
import com.padabajka.dating.testing.assertThrows
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
@UsesMocks(AuthRepository::class, MetadataRepository::class)
class LogOutUseCaseTest {

    private data class MockerRepositoryAndUseCase(
        val mocker: Mocker,
        val repository: AuthRepository,
        val useCase: LogOutUseCase
    )

    private fun getMockerRepositoryAndUseCase(): MockerRepositoryAndUseCase {
        val mocker = Mocker()
        val repository = MockAuthRepository(mocker)
        val metadataRepository = MockMetadataRepository(mocker)
        val deleteAuthMetadataUseCase = DeleteAuthMetadataUseCase(metadataRepository)
        val useCase = LogOutUseCase(repository, deleteAuthMetadataUseCase)
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
