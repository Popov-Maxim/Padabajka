package com.padabajka.dating.feature.auth.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.feature.auth.data.AuthRepositoryImpl
import com.padabajka.dating.feature.auth.data.local.LocalAuthDataSource
import com.padabajka.dating.feature.auth.data.model.AuthPreferences
import com.padabajka.dating.feature.auth.data.remote.FirebaseRemoteAuthDataSource
import com.padabajka.dating.feature.auth.data.remote.RemoteAuthDataSource
import com.padabajka.dating.feature.auth.domain.AuthStateProvider
import com.padabajka.dating.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.auth.domain.LoginEmailOnlyUseCase
import com.padabajka.dating.feature.auth.domain.OpenMailAppUseCase
import com.padabajka.dating.feature.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.padabajka.dating.feature.auth.domain.ReloadUserUseCase
import com.padabajka.dating.feature.auth.domain.SendEmailVerificationUseCase
import com.padabajka.dating.feature.auth.domain.ValidateEmailUseCase
import com.padabajka.dating.feature.auth.domain.ValidatePasswordsUseCase
import com.padabajka.dating.feature.auth.domain.method.GoogleLoginUseCase
import com.padabajka.dating.feature.auth.presentation.EmailLoginMethodComponent
import com.padabajka.dating.feature.auth.presentation.LoginMethodsComponent
import com.padabajka.dating.feature.auth.presentation.VerificationComponent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val authDataModule = module {
    factory {
        Firebase.auth
    }
    factory<RemoteAuthDataSource> {
        FirebaseRemoteAuthDataSource(
            firebaseAuth = get()
        )
    }
    single<AuthRepository> {
        AuthRepositoryImpl(
            remoteAuthDataSource = get(),
            localAuthDataSource = get()
        )
    }
    factory {
        LocalAuthDataSource(
            dataStore = DataStoreUtils.create(
                "auth_storage",
                AuthPreferences.serializer(),
                AuthPreferences()
            )
        )
    }
}

private val authDomainModule = module {
    factory {
        ValidateEmailUseCase()
    }

    factory {
        ValidatePasswordsUseCase()
    }
    factory {
        LogInWithEmailAndPasswordUseCase(
            authRepository = get()
        )
    }
    factory {
        RegisterWithEmailAndPasswordUseCase(
            authRepository = get(),
            validateEmailUseCase = { get() },
            validatePasswordsUseCase = { get() }
        )
    }

    factory {
        AuthStateProvider(
            authRepository = get()
        )
    }

    factory {
        LogOutUseCase(
            authRepository = get(),
            deleteAuthMetadataUseCase = get()
        )
    }
    factoryOf(::SendEmailVerificationUseCase)
    factoryOf(::ReloadUserUseCase)
    factoryOf(::LoginEmailOnlyUseCase)
    factoryOf(::GoogleLoginUseCase)
    factoryOf(::OpenMailAppUseCase)
}

expect val platformAuthDomainModule: Module

private val authPresentationModule = module {
    factory<LoginMethodsComponent> { parameters ->
        LoginMethodsComponent(
            context = parameters.get(),
            goToEmailMethodScreen = parameters.get(),
            googleLoginUseCase = get(),
        )
    }
    factory<EmailLoginMethodComponent> { parameters ->
        EmailLoginMethodComponent(
            context = parameters.get(),
            goToLoginMethodScreen = parameters.get(),
            validateEmailUseCase = get(),
            loginEmailOnlyUseCase = get(),
            openMailAppUseCase = get()
        )
    }
    factory<VerificationComponent> { parameters ->
        VerificationComponent(
            context = parameters.get(),
            sendEmailVerificationUseCaseFactory = { get() },
            reloadUserUseCaseFactory = { get() }
        )
    }
}

val authModules =
    arrayOf(authDataModule, authDomainModule, platformAuthDomainModule, authPresentationModule)
