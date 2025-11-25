package com.padabajka.dating.core.networking.di

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.KtorClientProviderImpl
import com.padabajka.dating.core.networking.UnauthKtorClientProvider
import com.padabajka.dating.core.networking.config.AuthenticatedConfigProvider
import com.padabajka.dating.core.networking.config.ContentNegotiationConfigProvider
import com.padabajka.dating.core.networking.config.HostConfigProvider
import com.padabajka.dating.core.networking.config.LoggingConfigProvider
import com.padabajka.dating.core.networking.config.RequestTimeoutConfigProvider
import com.padabajka.dating.core.networking.config.WebSocketConfigProvider
import org.koin.dsl.module

val networkingModule = module {
    single<KtorClientProvider> {
        val configProviders = listOf(
            AuthenticatedConfigProvider(authRepository = get()),
            ContentNegotiationConfigProvider(),
            HostConfigProvider(appSettings = get()),
            LoggingConfigProvider(),
            WebSocketConfigProvider(),
            RequestTimeoutConfigProvider()
        )
        KtorClientProviderImpl(configProviders)
    }

    single<UnauthKtorClientProvider> {
        val configProviders = listOf(
            ContentNegotiationConfigProvider(),
            HostConfigProvider(appSettings = get()),
            LoggingConfigProvider(),
            WebSocketConfigProvider(),
            RequestTimeoutConfigProvider()
        )
        KtorClientProviderImpl(configProviders)
    }
}
