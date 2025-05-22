package com.padabajka.dating.settings.di

import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.settings.data.MetadataRepositoryImpl
import com.padabajka.dating.settings.data.network.AuthMetadataApi
import com.padabajka.dating.settings.data.network.KtorAuthMetadataApi
import com.padabajka.dating.settings.data.source.MetadataRemoteDataSource
import com.padabajka.dating.settings.data.source.MetadataRemoteDataSourceImpl
import com.padabajka.dating.settings.domain.DeleteAuthMetadataUseCase
import com.padabajka.dating.settings.domain.NewAuthMetadataUseCase
import com.padabajka.dating.settings.domain.UpdateAuthMetadataUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.installations.FirebaseInstallations
import dev.gitlive.firebase.installations.installations
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataModule = module {
    single<MetadataRepository> {
        MetadataRepositoryImpl(
            firebaseInstallations = get(),
            remoteDataSource = get()
        )
    }

    factory<FirebaseInstallations> {
        Firebase.installations
    }

    factory<MetadataRemoteDataSource> {
        MetadataRemoteDataSourceImpl(
            metadataApi = get()
        )
    }

    factory<AuthMetadataApi> {
        KtorAuthMetadataApi(
            ktorClientProvider = get()
        )
    }
}

private val domainModule = module {
    factoryOf(::DeleteAuthMetadataUseCase)
    factoryOf(::NewAuthMetadataUseCase)
    factoryOf(::UpdateAuthMetadataUseCase)
}

val metadataDiModules = arrayOf(domainModule, dataModule)
