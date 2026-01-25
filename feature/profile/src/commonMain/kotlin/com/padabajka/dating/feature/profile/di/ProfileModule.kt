package com.padabajka.dating.feature.profile.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.profile.DraftProfile
import com.padabajka.dating.feature.image.domain.LoadImageUseCase
import com.padabajka.dating.feature.profile.data.DraftProfileRepositoryImpl
import com.padabajka.dating.feature.profile.data.ProfileRepositoryImpl
import com.padabajka.dating.feature.profile.data.asset.AssetRepositoryImpl
import com.padabajka.dating.feature.profile.data.asset.CityRepositoryImpl
import com.padabajka.dating.feature.profile.data.asset.model.AssetVersionsDto
import com.padabajka.dating.feature.profile.data.asset.network.CityApi
import com.padabajka.dating.feature.profile.data.asset.network.InterestAssetApi
import com.padabajka.dating.feature.profile.data.asset.network.LanguageAssetApi
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetVersionsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSourceImpl
import com.padabajka.dating.feature.profile.data.asset.source.RemoteAssetDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSourceImpl
import com.padabajka.dating.feature.profile.data.network.KtorProfileApi
import com.padabajka.dating.feature.profile.data.network.ProfileApi
import com.padabajka.dating.feature.profile.data.source.DataStoreLocalDraftProfileDataSource
import com.padabajka.dating.feature.profile.data.source.LocalDraftProfileDataSource
import com.padabajka.dating.feature.profile.data.source.RemoveProfileDataSource
import com.padabajka.dating.feature.profile.data.source.RemoveProfileDataSourceImpl
import com.padabajka.dating.feature.profile.domain.CreateProfileFromDraftUseCase
import com.padabajka.dating.feature.profile.domain.SaveUpdatedProfileUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindCitiesUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindInterestAssetsUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindLanguageAssetsUseCase
import com.padabajka.dating.feature.profile.domain.creator.BirthdayValidator
import com.padabajka.dating.feature.profile.domain.creator.DraftProfileProvider
import com.padabajka.dating.feature.profile.domain.creator.NameValidator
import com.padabajka.dating.feature.profile.domain.update.AboutMeUpdateUseCase
import com.padabajka.dating.feature.profile.domain.update.BirthdayUpdateUseCase
import com.padabajka.dating.feature.profile.domain.update.FirstNameUpdateUseCase
import com.padabajka.dating.feature.profile.domain.update.UpdateLookingForUseCase
import com.padabajka.dating.feature.profile.domain.update.UpdateMainImageUseCase
import com.padabajka.dating.feature.profile.domain.update.UpdateUserGenderUseCase
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
            coroutineScope = get(),
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
            dataStore = DataStoreUtils.createFake(DraftProfile())
        )
    }

    single<CityRepositoryImpl> {
        CityRepositoryImpl(
            remoteCityDataSource = get(),
            localCityDataSource = get(),
            localAssetVersionsDataSource = get(),
            localAssetDataSource = get()
        )
    }

    single<AssetRepository> {
        AssetRepositoryImpl(
            localAssetDataSource = get(),
            localCityDataSource = get(),
            settingsRepository = get(),
            remoteAssetDataSource = get(),
            cityRepository = get(),
            localAssetVersionsDataSource = get()
        )
    }

    factoryOf(::RemoteAssetDataSource)
    factoryOf(::InterestAssetApi)
    factoryOf(::LanguageAssetApi)
    factoryOf(::LocalAssetsDataSource)
    single<LocalAssetVersionsDataSource> {
        LocalAssetVersionsDataSource(
            assetVersionsDataStore = DataStoreUtils.create(
                "asset_versions_storage",
                AssetVersionsDto.serializer(),
                AssetVersionsDto.default()
            )
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

    factoryOf(::SaveUpdatedProfileUseCase)

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

    factoryOf(::BirthdayUpdateUseCase)

    factoryOf(::FindCitiesUseCase)
    factoryOf(::FindLanguageAssetsUseCase)
    factoryOf(::NameValidator)
    factoryOf(::DraftProfileProvider)
    factoryOf(::BirthdayValidator)
    factoryOf(::UpdateUserGenderUseCase)
    factoryOf(::UpdateLookingForUseCase)
    factoryOf(::UpdateMainImageUseCase)
    factoryOf(::CreateProfileFromDraftUseCase)
    factoryOf(::LoadImageUseCase)
    factoryOf(::FindInterestAssetsUseCase)
}

private val presentationModule = module {
    factory<ProfileScreenComponent> { parameters ->
        ProfileScreenComponent(
            context = parameters.get(),
            openEditor = parameters.get(),
            openLikesMeScreen = parameters.get(),
            profileRepository = get()
        )
    }

    factory<ProfileEditorScreenComponent> { parameters ->
        ProfileEditorScreenComponent(
            context = parameters.get(),
            navigateBack = parameters.get(),
            profileRepository = get(),
            saveUpdatedProfileUseCaseFactory = { get() },
            getLocalImageUseCaseFactory = { get() },
            findCitiesUseCase = get(),
            findLanguageAssetsUseCase = get(),
            findInterestAssetsUseCase = get()
        )
    }
}

val profileModules = arrayOf(dataModule, domainModule, presentationModule)
