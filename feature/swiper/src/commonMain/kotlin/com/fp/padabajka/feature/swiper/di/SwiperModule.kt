package com.fp.padabajka.feature.swiper.di

import androidx.datastore.core.DataStore
import com.fp.padabajka.core.repository.api.CardRepository
import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.ReactionRepository
import com.fp.padabajka.core.repository.api.SearchPreferencesRepository
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.CardRepositoryImpl
import com.fp.padabajka.feature.swiper.data.CardSelector
import com.fp.padabajka.feature.swiper.data.CardSelectorProvider
import com.fp.padabajka.feature.swiper.data.NoAdCardSelector
import com.fp.padabajka.feature.swiper.data.person.PersonRepositoryImpl
import com.fp.padabajka.feature.swiper.data.person.network.KtorPersonApi
import com.fp.padabajka.feature.swiper.data.person.network.PersonApi
import com.fp.padabajka.feature.swiper.data.person.source.RemotePersonDataSource
import com.fp.padabajka.feature.swiper.data.person.source.RemotePersonDataSourceImpl
import com.fp.padabajka.feature.swiper.data.reaction.ReactionRepositoryImpl
import com.fp.padabajka.feature.swiper.data.reaction.network.FakeReactionApi
import com.fp.padabajka.feature.swiper.data.reaction.network.ReactionApi
import com.fp.padabajka.feature.swiper.data.reaction.source.RemoteReactionDataSource
import com.fp.padabajka.feature.swiper.data.reaction.source.RemoteReactionDataSourceImpl
import com.fp.padabajka.feature.swiper.data.search.SearchPreferencesRepositoryImpl
import com.fp.padabajka.feature.swiper.data.search.model.SearchPrefDto
import com.fp.padabajka.feature.swiper.data.search.model.toDto
import com.fp.padabajka.feature.swiper.data.search.source.LocalSearchPreferencesDataSource
import com.fp.padabajka.feature.swiper.data.search.source.LocalSearchPreferencesDataSourceImpl
import com.fp.padabajka.feature.swiper.domain.NextCardUseCase
import com.fp.padabajka.feature.swiper.domain.ReactToCardUseCase
import com.fp.padabajka.feature.swiper.domain.search.SearchPreferencesProvider
import com.fp.padabajka.feature.swiper.domain.search.UpdateSearchPrefUseCase
import com.fp.padabajka.feature.swiper.presentation.SwiperScreenComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet
import org.koin.dsl.module

private val dataModule = module {

    single<CardRepository> {
        CardRepositoryImpl(
            personRepository = get(),
            nativeAdRepository = get(),
            reactionRepository = get(),
            cardSelectorProvider = get()
        )
    }

    single<PersonRepository> {
        PersonRepositoryImpl(
            scope = get(),
            remotePersonDataSource = get()
        )
    }

    factory<RemotePersonDataSource> {
        RemotePersonDataSourceImpl(
            personApi = get()
        )
    }

    factory<PersonApi> {
        KtorPersonApi(
            ktorClientProvider = get()
        )
    }

    single<ReactionRepository> {
        ReactionRepositoryImpl(
            remoteReactionDataSource = get()
        )
    }

    factory<RemoteReactionDataSource> {
        RemoteReactionDataSourceImpl(
            reactionApi = get(),
            scope = get()
        )
    }

    factory<ReactionApi> {
        FakeReactionApi()
    }

    factory<CardSelectorProvider> {
        CardSelectorProvider(
            cardSelectorFactory = { get() }
        )
    }

    factory<CardSelector> {
        NoAdCardSelector()
    }

    single<SearchPreferencesRepository> {
        SearchPreferencesRepositoryImpl(
            localDataSource = get()
        )
    }

    factory<LocalSearchPreferencesDataSource> {
        LocalSearchPreferencesDataSourceImpl(
            dataStore = object : DataStore<SearchPrefDto> {
                private val _data = MutableStateFlow(SearchPreferences.DEFAULT.toDto())
                override val data: Flow<SearchPrefDto> = _data

                override suspend fun updateData(
                    transform: suspend (t: SearchPrefDto) -> SearchPrefDto
                ): SearchPrefDto {
                    return _data.updateAndGet { transform(it) }
                }
            } // TODO(datastore): add init
        )
    }
}

private val domainModule = module {
    factory<ReactToCardUseCase> {
        ReactToCardUseCase(
            cardRepository = get()
        )
    }

    factory<NextCardUseCase> {
        NextCardUseCase(
            cardRepository = get(),
            searchPreferencesRepository = get()
        )
    }

    factory<UpdateSearchPrefUseCase> {
        UpdateSearchPrefUseCase(
            searchPreferencesRepository = get()
        )
    }

    factory<SearchPreferencesProvider> {
        SearchPreferencesProvider(
            searchPreferencesRepository = get()
        )
    }
}

private val presentationModule = module {
    factory<SwiperScreenComponent> { parameters ->
        SwiperScreenComponent(
            context = parameters.get(),
            reactToCardUseCaseFactory = { get() },
            nextCardUseCaseFactory = { get() },
            updateSearchPrefUseCaseFactory = { get() },
            searchPreferencesProvider = get()
        )
    }
}

val swiperModules = arrayOf(dataModule, domainModule, presentationModule)
