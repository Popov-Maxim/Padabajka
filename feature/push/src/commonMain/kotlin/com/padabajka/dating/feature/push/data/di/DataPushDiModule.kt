package com.padabajka.dating.feature.push.data.di

import com.padabajka.dating.core.repository.api.PushRepository
import com.padabajka.dating.feature.push.data.data.PushRepositoryImpl
import com.padabajka.dating.feature.push.data.data.network.KtorTokenApi
import com.padabajka.dating.feature.push.data.data.network.TokenApi
import com.padabajka.dating.feature.push.data.data.source.RemoteDataSource
import com.padabajka.dating.feature.push.data.data.source.RemoteDataSourceImpl
import com.padabajka.dating.feature.push.data.domain.DataPushParser
import com.padabajka.dating.feature.push.data.domain.HandleNewMatchUseCase
import com.padabajka.dating.feature.push.data.domain.HandlePushUseCase
import com.padabajka.dating.feature.push.data.domain.SaveTokenUseCase
import com.padabajka.dating.feature.push.data.domain.UpdateTokenUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataPushDiModule = module {
    single<PushRepository> {
        PushRepositoryImpl(
            remoteDataSource = get()
        )
    }

    factory<RemoteDataSource> {
        RemoteDataSourceImpl(
            api = get()
        )
    }

    factory<TokenApi> {
        KtorTokenApi(
            ktorClientProvider = get()
        )
    }

    factoryOf(::UpdateTokenUseCase)
    factoryOf(::SaveTokenUseCase)
    factoryOf(::HandlePushUseCase)
    factoryOf(::HandleNewMatchUseCase)
    factoryOf(::DataPushParser)
}
