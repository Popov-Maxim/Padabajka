package com.padabajka.dating.feature.push.socket.di

import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.feature.push.socket.data.KtorSocketRepository
import com.padabajka.dating.feature.push.socket.data.network.KtorSocketApi
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataDiModule = module {
    factory<SocketRepository> { parameters ->
        KtorSocketRepository(
            userId = parameters.get<UserId>(),
            scope = parameters.get<CoroutineScope>(),
            ktorSocketApi = get(),
            metadataRepository = get(),
            authRepository = get(),
        )
    }

    factoryOf(::KtorSocketApi)
}

private val domainDiModule = module {
}

val socketDiModules = arrayOf(domainDiModule, dataDiModule)
