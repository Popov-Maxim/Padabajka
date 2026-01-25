package com.padabajka.dating.feature.swiper.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.CandidateRepository
import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.data.CardRepositoryImpl
import com.padabajka.dating.feature.swiper.data.CardSelector
import com.padabajka.dating.feature.swiper.data.CardSelectorProvider
import com.padabajka.dating.feature.swiper.data.NoAdCardSelector
import com.padabajka.dating.feature.swiper.data.candidate.CandidateRepositoryImpl
import com.padabajka.dating.feature.swiper.data.candidate.network.CandidateApi
import com.padabajka.dating.feature.swiper.data.candidate.network.KtorCandidateApi
import com.padabajka.dating.feature.swiper.data.candidate.source.RemoteCandidateDataSource
import com.padabajka.dating.feature.swiper.data.candidate.source.RemoteCandidateDataSourceImpl
import com.padabajka.dating.feature.swiper.data.reaction.ReactionRepositoryImpl
import com.padabajka.dating.feature.swiper.data.reaction.network.KtorReactionApi
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionApi
import com.padabajka.dating.feature.swiper.data.reaction.source.LocalReactionDataSource
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
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataModule = module {

    single<CardRepository> {
        CardRepositoryImpl(
            candidateRepository = get(),
            nativeAdRepository = get(),
            reactionRepository = get(),
            cardSelectorProvider = get()
        )
    }

    single<CandidateRepository> {
        CandidateRepositoryImpl(
            scope = get(),
            remoteCandidateDataSource = get()
        )
    }

    factory<RemoteCandidateDataSource> {
        RemoteCandidateDataSourceImpl(
            candidateApi = get()
        )
    }

    factory<CandidateApi> {
        KtorCandidateApi(
            ktorClientProvider = get()
        )
    }

    single<ReactionRepository> {
        ReactionRepositoryImpl(
            scope = get(),
            remoteReactionDataSource = get(),
            localReactionDataSource = get(),
            authRepository = get()
        )
    }

    factoryOf(::LocalReactionDataSource)

    factory<RemoteReactionDataSource> {
        RemoteReactionDataSourceImpl(
            reactionApi = get(),
            scope = get()
        )
    }

    factory<ReactionApi> {
        KtorReactionApi(
            ktorClientProvider = get()
        )
//        FakeReactionApi(
//            matchRepository = get(),
//            personRepository = get(),
//            notificationService = get()
//        )
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
