package com.fp.padabajka.feature.profile.di

import com.fp.padabajka.core.data.utils.DataStoreUtils
import com.fp.padabajka.core.repository.api.EditProfileRepository
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.feature.profile.data.EditProfileRepositoryImpl
import com.fp.padabajka.feature.profile.data.ProfileRepositoryImpl
import com.fp.padabajka.feature.profile.data.network.KtorProfileApi
import com.fp.padabajka.feature.profile.data.network.ProfileApi
import com.fp.padabajka.feature.profile.data.source.DataStoreLocalEditProfileDataSource
import com.fp.padabajka.feature.profile.data.source.LocalEditProfileDataSource
import com.fp.padabajka.feature.profile.data.source.RemoveProfileDataSource
import com.fp.padabajka.feature.profile.data.source.RemoveProfileDataSourceImpl
import com.fp.padabajka.feature.profile.domain.DiscardUpdateUseCase
import com.fp.padabajka.feature.profile.domain.ProfileEditorProvider
import com.fp.padabajka.feature.profile.domain.SaveUpdateProfileUseCase
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

    single<EditProfileRepository> {
        EditProfileRepositoryImpl(
            profileRepository = get(),
            localEditProfileDataSource = get()
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

    factory<LocalEditProfileDataSource> {
        DataStoreLocalEditProfileDataSource(
            dataStore = DataStoreUtils.createFake(null)
        )
    }
}

private val domainModule = module {

    factory<ProfileEditorProvider> {
        ProfileEditorProvider(
            editProfileRepository = get()
        )
    }

    factory<DiscardUpdateUseCase> {
        DiscardUpdateUseCase(
            repository = get()
        )
    }

    factory<SaveUpdateProfileUseCase> {
        SaveUpdateProfileUseCase(
            repository = get()
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
            profileRepository = get()
        )
    }

    factory<ProfileEditorScreenComponent> { parameters ->
        ProfileEditorScreenComponent(
            context = parameters.get(),
            profileRepository = get(),
            profileEditorProvider = get(),
            discardUpdateUseCaseFactory = { get() },
            saveUpdateProfileUseCaseFactory = { get() },
            firstNameUpdateUseCaseFactory = { get() },
            lastNameUpdateUseCaseFactory = { get() },
            aboutMeUpdateUseCaseFactory = { get() },
            hideAchievementUseCaseFactory = { get() },
            makeAchievementVisibleUseCaseFactory = { get() },
            mainAchievementUpdateUseCaseFactory = { get() }
        )
    }
}

val profileModules = arrayOf(dataModule, domainModule, presentationModule)
