package com.padabajka.dating.feature.match.di

import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.feature.match.data.MatchRepositoryImpl
import com.padabajka.dating.feature.match.data.PersonRepositoryImpl
import com.padabajka.dating.feature.match.data.source.local.LocalMatchDataSource
import com.padabajka.dating.feature.match.data.source.local.LocalPersonDataSource
import com.padabajka.dating.feature.match.data.source.local.RoomLocalMatchDataSource
import com.padabajka.dating.feature.match.data.source.local.RoomLocalPersonDataSource
import com.padabajka.dating.feature.match.data.source.remote.KtorMatchesApi
import com.padabajka.dating.feature.match.data.source.remote.MatchesApi
import com.padabajka.dating.feature.match.data.source.remote.RemoteMatchDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataModule = module {
    single<PersonRepository> {
        PersonRepositoryImpl(
            localPersonDataSource = get(),
            profileRepository = get()
        )
    }

    single<MatchRepository> {
        MatchRepositoryImpl(
            localMatchDataSource = get(),
            remoteMatchDataSource = get(),
            personRepository = get()
        )
    }

    factory<LocalPersonDataSource> {
        RoomLocalPersonDataSource(
            personDao = get()
        )
    }

    factory<LocalMatchDataSource> {
        RoomLocalMatchDataSource(
            matchesDao = get()
        )
    }

    factoryOf(::RemoteMatchDataSource)
    factory<MatchesApi> {
        KtorMatchesApi(
            ktorClientProvider = get()
        )
    }
}

val matchDiModules = arrayOf(dataModule)
