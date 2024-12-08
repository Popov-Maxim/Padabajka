package com.fp.padabajka.core.networking

import com.fp.padabajka.core.networking.config.KtorConfigProvider
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

interface KtorClientProvider {
    suspend fun client(): HttpClient
}

internal class KtorClientProviderImpl(
    private val configProviders: List<KtorConfigProvider>,
    private val engineFactory: HttpClientEngineFactory<*> = CIO,
) : KtorClientProvider {

    private val dynamicConfigProviders: List<KtorConfigProvider.Dynamic> by lazy {
        configProviders.filterIsInstance<KtorConfigProvider.Dynamic>()
    }

    private val staticConfig: HttpClientConfig<*> by lazy {
        configProviders.filterIsInstance<KtorConfigProvider.Static>().resultConfig()
    }

    override suspend fun client(): HttpClient {
        val dynamicConfig = dynamicConfigProviders.resultConfig()

        return HttpClient(engineFactory) {
            plusAssign(dynamicConfig)
            plusAssign(staticConfig)
        }
    }

    private suspend fun List<KtorConfigProvider.Dynamic>.resultConfig(): HttpClientConfig<Nothing> {
        return map { it.config() }.sum()
    }

    private fun List<KtorConfigProvider.Static>.resultConfig(): HttpClientConfig<Nothing> {
        return map { it.config }.sum()
    }

    private fun List<HttpClientConfig<Nothing>>.sum(): HttpClientConfig<Nothing> {
        return reduceOrNull { acc, ktorConfigProvider ->
            acc.plusAssign(ktorConfigProvider)
            acc
        } ?: HttpClientConfig()
    }
}