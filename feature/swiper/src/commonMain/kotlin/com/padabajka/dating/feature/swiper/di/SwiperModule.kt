package com.padabajka.dating.feature.swiper.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.data.CardRepositoryImpl
import com.padabajka.dating.feature.swiper.data.CardSelector
import com.padabajka.dating.feature.swiper.data.CardSelectorProvider
import com.padabajka.dating.feature.swiper.data.NoAdCardSelector
import com.padabajka.dating.feature.swiper.data.person.PersonRepositoryImpl
import com.padabajka.dating.feature.swiper.data.person.network.KtorPersonApi
import com.padabajka.dating.feature.swiper.data.person.network.PersonApi
import com.padabajka.dating.feature.swiper.data.person.source.RemotePersonDataSource
import com.padabajka.dating.feature.swiper.data.person.source.RemotePersonDataSourceImpl
import com.padabajka.dating.feature.swiper.data.reaction.ReactionRepositoryImpl
import com.padabajka.dating.feature.swiper.data.reaction.network.FakeReactionApi
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionApi
import com.padabajka.dating.feature.swiper.data.reaction.source.RemoteReactionDataSource
import com.padabajka.dating.feature.swiper.data.reaction.source.RemoteReactionDataSourceImpl
import com.padabajka.dating.feature.swiper.data.search.SearchPreferencesRepositoryImpl
import com.padabajka.dating.feature.swiper.data.search.model.toDto
import com.padabajka.dating.feature.swiper.data.search.source.LocalSearchPreferencesDataSource
import com.padabajka.dating.feature.swiper.data.search.source.LocalSearchPreferencesDataSourceImpl
import com.padabajka.dating.feature.swiper.domain.NextCardUseCase
import com.padabajka.dating.feature.swiper.domain.ReactToCardUseCase
import com.padabajka.dating.feature.swiper.domain.search.SearchPreferencesProvider
import com.padabajka.dating.feature.swiper.domain.search.UpdateSearchPrefUseCase
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
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
            dataStore = DataStoreUtils.createFake(SearchPreferences.DEFAULT.toDto())
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
