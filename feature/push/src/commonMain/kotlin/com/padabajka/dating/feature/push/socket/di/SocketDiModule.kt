package com.padabajka.dating.feature.push.socket.di

import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.feature.push.socket.data.KtorSocketRepository
import com.padabajka.dating.feature.push.socket.data.network.KtorSocketApi
import com.padabajka.dating.feature.push.socket.domain.SocketMessageObserver
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataDiModule = module {
    single<SocketRepository> {
        KtorSocketRepository(
            ktorSocketApi = get(),
            metadataRepository = get()
        )
    }

    factoryOf(::KtorSocketApi)
}

private val domainDiModule = module {
    factoryOf(::SocketMessageObserver)
}

val socketDiModules = arrayOf(domainDiModule, dataDiModule)
