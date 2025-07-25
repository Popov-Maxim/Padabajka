package com.padabajka.dating.feature.profile.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.CityRepository
import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.feature.profile.data.DraftProfileRepositoryImpl
import com.padabajka.dating.feature.profile.data.ProfileRepositoryImpl
import com.padabajka.dating.feature.profile.data.asset.CityRepositoryImpl
import com.padabajka.dating.feature.profile.data.asset.network.CityApi
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSourceImpl
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSourceImpl
import com.padabajka.dating.feature.profile.data.network.KtorProfileApi
import com.padabajka.dating.feature.profile.data.network.ProfileApi
import com.padabajka.dating.feature.profile.data.source.DataStoreLocalDraftProfileDataSource
import com.padabajka.dating.feature.profile.data.source.LocalDraftProfileDataSource
import com.padabajka.dating.feature.profile.data.source.RemoveProfileDataSource
import com.padabajka.dating.feature.profile.data.source.RemoveProfileDataSourceImpl
import com.padabajka.dating.feature.profile.domain.DiscardUpdateUseCase
import com.padabajka.dating.feature.profile.domain.DraftProfileProvider
import com.padabajka.dating.feature.profile.domain.SaveProfileUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindCitiesUseCase
import com.padabajka.dating.feature.profile.domain.update.AboutMeUpdateUseCase
import com.padabajka.dating.feature.profile.domain.update.FirstNameUpdateUseCase
import com.padabajka.dating.feature.profile.domain.update.HideAchievementUseCase
import com.padabajka.dating.feature.profile.domain.update.MainAchievementUpdateUseCase
import com.padabajka.dating.feature.profile.domain.update.MakeAchievementVisibleUseCase
import com.padabajka.dating.feature.profile.presentation.ProfileScreenComponent
import com.padabajka.dating.feature.profile.presentation.editor.ProfileEditorScreenComponent
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            removeProfileDataSource = get()
        )
    }

    single<DraftProfileRepository> {
        DraftProfileRepositoryImpl(
            profileRepository = get(),
            localDraftProfileDataSource = get()
        )
    }

    factory<RemoveProfileDataSource> {
        RemoveProfileDataSourceImpl(
            profileApi = get()
        )
    }

    factory<ProfileApi> {
        KtorProfileApi(
            ktorClientProvider = get()
        )
    }

    factory<LocalDraftProfileDataSource> {
        DataStoreLocalDraftProfileDataSource(
            dataStore = DataStoreUtils.createFake(null)
        )
    }

    single<CityRepository> {
        CityRepositoryImpl(
            remoteCityDataSource = get(),
            localCityDataSource = get()
        )
    }

    factory<RemoteCityDataSource> {
        RemoteCityDataSourceImpl(
            cityApi = get()
        )
    }

    factoryOf(::CityApi)

    factory<LocalCityDataSource> {
        LocalCityDataSourceImpl(
            cityDao = get()
        )
    }
}

private val domainModule = module {

    factory<DraftProfileProvider> {
        DraftProfileProvider(
            draftProfileRepository = get()
        )
    }

    factory<DiscardUpdateUseCase> {
        DiscardUpdateUseCase(
            repository = get()
        )
    }

    factoryOf(::SaveProfileUseCase)

    factory<FirstNameUpdateUseCase> {
        FirstNameUpdateUseCase(
            repository = get()
        )
    }

    factory<AboutMeUpdateUseCase> {
        AboutMeUpdateUseCase(
            repository = get()
        )
    }

    factory<HideAchievementUseCase> {
        HideAchievementUseCase(
            repository = get()
        )
    }

    factory<MakeAchievementVisibleUseCase> {
        MakeAchievementVisibleUseCase(
            repository = get()
        )
    }

    factory<MainAchievementUpdateUseCase> {
        MainAchievementUpdateUseCase(
            repository = get()
        )
    }

    factoryOf(::FindCitiesUseCase)
}

private val presentationModule = module {
    factory<ProfileScreenComponent> { parameters ->
        ProfileScreenComponent(
            context = parameters.get(),
            openEditor = parameters.get(),
            profileRepository = get(),
            logoutUseCaseFactory = { get() }
        )
    }

    factory<ProfileEditorScreenComponent> { parameters ->
        ProfileEditorScreenComponent(
            context = parameters.get(),
            navigateBack = parameters.get(),
            profileRepository = get(),
            saveProfileUseCaseFactory = { get() },
            getLocalImageUseCaseFactory = { get() },
            findCitiesUseCase = get()
        )
    }
}

val profileModules = arrayOf(dataModule, domainModule, presentationModule)
