package com.fp.padabajka.feature.auth.di

import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.domain.ValidatePasswordsUseCase
import org.koin.dsl.module

private val authDataModule = module {
    // TODO: Add dependencies
}

private val authDomainModule = module {
    single {
        Factory { ValidateEmailUseCase() }
    }
    single {
        Factory { ValidatePasswordsUseCase() }
    }
    single {
        Factory { LogInWithEmailAndPasswordUseCase(get()) }
    }
    single {
        Factory { RegisterWithEmailAndPasswordUseCase(get(), get(), get()) }
    }
}

private val authPresentationModule = module {
    // TODO: Add component scoped dependency
}

val authModules = arrayOf(authDataModule, authDomainModule, authPresentationModule)
