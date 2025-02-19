package com.fp.padabajka.core.networking.di

import com.fp.padabajka.core.networking.KtorClientProvider
import com.fp.padabajka.core.networking.KtorClientProviderImpl
import com.fp.padabajka.core.networking.config.AuthenticatedConfigProvider
import com.fp.padabajka.core.networking.config.ContentNegotiationConfigProvider
import com.fp.padabajka.core.networking.config.HostConfigProvider
import com.fp.padabajka.core.networking.config.LoggingConfigProvider
import org.koin.dsl.module

val networkingModule = module {
    single<KtorClientProvider> {
        val configProviders = listOf(
            AuthenticatedConfigProvider(authRepository = get()),
            ContentNegotiationConfigProvider(),
            HostConfigProvider(appSettings = get()),
            LoggingConfigProvider()
        )
        KtorClientProviderImpl(configProviders)
    }
}
