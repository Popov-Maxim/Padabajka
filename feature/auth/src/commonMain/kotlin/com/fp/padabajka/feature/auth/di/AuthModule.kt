package com.fp.padabajka.feature.auth.di

import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.repository.api.AuthRepository
import com.fp.padabajka.feature.auth.data.AuthRepositoryImpl
import com.fp.padabajka.feature.auth.data.remote.FirebaseRemoteAuthDataSource
import com.fp.padabajka.feature.auth.data.remote.RemoteAuthDataSource
import com.fp.padabajka.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.domain.ValidatePasswordsUseCase
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
        Factory { ValidateEmailUseCase() }
    }
    factory {
        Factory { ValidatePasswordsUseCase() }
    }
    factory {
        Factory { LogInWithEmailAndPasswordUseCase(get()) }
    }
    factory {
        Factory { RegisterWithEmailAndPasswordUseCase(get(), get(), get()) }
    }
}

private val authPresentationModule = module {
    // TODO: Add component scoped dependency
}

val authModules = arrayOf(authDataModule, authDomainModule, authPresentationModule)
