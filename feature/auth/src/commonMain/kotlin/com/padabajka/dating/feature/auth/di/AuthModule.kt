package com.padabajka.dating.feature.auth.di

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.feature.auth.data.AuthRepositoryImpl
import com.padabajka.dating.feature.auth.data.remote.FirebaseRemoteAuthDataSource
import com.padabajka.dating.feature.auth.data.remote.RemoteAuthDataSource
import com.padabajka.dating.feature.auth.domain.AuthStateProvider
import com.padabajka.dating.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.padabajka.dating.feature.auth.domain.ReloadUserUseCase
import com.padabajka.dating.feature.auth.domain.SendEmailVerificationUseCase
import com.padabajka.dating.feature.auth.domain.ValidateEmailUseCase
import com.padabajka.dating.feature.auth.domain.ValidatePasswordsUseCase
import com.padabajka.dating.feature.auth.presentation.LoginComponent
import com.padabajka.dating.feature.auth.presentation.RegisterComponent
import com.padabajka.dating.feature.auth.presentation.VerificationComponent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.dsl.factoryOf
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
            authRepository = get()
        )
    }
    factoryOf(::SendEmailVerificationUseCase)
    factoryOf(::ReloadUserUseCase)
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
    factory<VerificationComponent> { parameters ->
        VerificationComponent(
            context = parameters.get(),
            sendEmailVerificationUseCaseFactory = { get() },
            reloadUserUseCaseFactory = { get() }
        )
    }
}

val authModules = arrayOf(authDataModule, authDomainModule, authPresentationModule)
