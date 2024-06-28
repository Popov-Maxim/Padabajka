package com.fp.padabajka.feature.auth.di

import com.fp.padabajka.core.repository.api.AuthRepository
import com.fp.padabajka.feature.auth.data.AuthRepositoryImpl
import com.fp.padabajka.feature.auth.data.remote.FirebaseRemoteAuthDataSource
import com.fp.padabajka.feature.auth.data.remote.RemoteAuthDataSource
import com.fp.padabajka.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.domain.ValidatePasswordsUseCase
import com.fp.padabajka.feature.auth.presentation.LoginComponent
import com.fp.padabajka.feature.auth.presentation.RegisterComponent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.dsl.module

private val authDataModule = module {
    factory {
        Firebase.auth
    }
    factory<RemoteAuthDataSource> {
        FirebaseRemoteAuthDataSource(get())
    }
    single<AuthRepository> {
        AuthRepositoryImpl(get())
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
            validateEmailUseCase = get(),
            validatePasswordsUseCase = get()
        )
    }
}

private val authPresentationModule = module {
    factory<LoginComponent> { parameters ->
        LoginComponent(
            context = parameters.get(),
            goToRegister = parameters.get(),
            logInWithEmailAndPasswordUseCaseFactory = { get() },
            validateEmailUseCaseFactory = { get() }
        )
    }
    factory<RegisterComponent> { parameters ->
        RegisterComponent(
            context = parameters.get(),
            goToLogin = parameters.get(),
            registerWithEmailAndPasswordUseCaseFactory = { get() },
            validateEmailUseCaseFactory = { get() },
            validatePasswordsUseCaseFactory = { get() },
        )
    }
}

val authModules = arrayOf(authDataModule, authDomainModule, authPresentationModule)
