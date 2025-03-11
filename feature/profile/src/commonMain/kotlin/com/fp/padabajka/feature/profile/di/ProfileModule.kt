package com.fp.padabajka.feature.profile.di

import com.fp.padabajka.core.data.utils.DataStoreUtils
import com.fp.padabajka.core.repository.api.DraftProfileRepository
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.feature.profile.data.DraftProfileRepositoryImpl
import com.fp.padabajka.feature.profile.data.ProfileRepositoryImpl
import com.fp.padabajka.feature.profile.data.network.KtorProfileApi
import com.fp.padabajka.feature.profile.data.network.ProfileApi
import com.fp.padabajka.feature.profile.data.source.DataStoreLocalDraftProfileDataSource
import com.fp.padabajka.feature.profile.data.source.LocalDraftProfileDataSource
import com.fp.padabajka.feature.profile.data.source.RemoveProfileDataSource
import com.fp.padabajka.feature.profile.data.source.RemoveProfileDataSourceImpl
import com.fp.padabajka.feature.profile.domain.DiscardUpdateUseCase
import com.fp.padabajka.feature.profile.domain.DraftProfileProvider
import com.fp.padabajka.feature.profile.domain.SaveProfileUseCase
import com.fp.padabajka.feature.profile.domain.update.AboutMeUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.FirstNameUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.HideAchievementUseCase
import com.fp.padabajka.feature.profile.domain.update.LastNameUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.MainAchievementUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.MakeAchievementVisibleUseCase
import com.fp.padabajka.feature.profile.presentation.ProfileScreenComponent
import com.fp.padabajka.feature.profile.presentation.editor.ProfileEditorScreenComponent
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

    factory<SaveProfileUseCase> {
        SaveProfileUseCase(
            profileRepository = get()
        )
    }

    factory<FirstNameUpdateUseCase> {
        FirstNameUpdateUseCase(
            repository = get()
        )
    }

    factory<LastNameUpdateUseCase> {
        LastNameUpdateUseCase(
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
            profileRepository = get(),
            saveProfileUseCaseFactory = { get() },
        )
    }
}

val profileModules = arrayOf(dataModule, domainModule, presentationModule)
