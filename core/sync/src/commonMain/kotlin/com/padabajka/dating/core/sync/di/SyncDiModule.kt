package com.padabajka.dating.core.sync.di

import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.sync.SyncManager
import com.padabajka.dating.core.sync.SyncManagerFactory
import com.padabajka.dating.core.sync.SyncSessionObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

expect val platformSyncDiModule: Module

private val common = module {
    single<SyncManagerFactory> {
        val appScope = get<CoroutineScope>()
        SyncManagerFactory { userId ->
            val sessionJob = SupervisorJob(appScope.coroutineContext[Job])
            val sessionScope = CoroutineScope(appScope.coroutineContext + sessionJob)
            runCatching {
                SyncManager(
                    userId = userId,
                    scope = sessionScope,
                    socketRepository = get<SocketRepository> { parametersOf(userId, sessionScope) },
                    syncRemoteDataUseCase = get(),
                    handlePushUseCase = get(),
                    authRepository = get(),
                )
            }.onFailure {
                sessionJob.cancel()
            }.getOrThrow()
        }
    }
    singleOf(::SyncSessionObserver)
}

val syncDiModule = arrayOf(common, platformSyncDiModule)
