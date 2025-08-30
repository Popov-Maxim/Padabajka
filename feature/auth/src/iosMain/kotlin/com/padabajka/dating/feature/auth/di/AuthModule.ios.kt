package com.padabajka.dating.feature.auth.di

import com.padabajka.dating.feature.auth.domain.method.GoogleAuthTokenProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val platformAuthDomainModule: Module = module {
    factoryOf(::GoogleAuthTokenProvider)
}
