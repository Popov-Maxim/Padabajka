package com.fp.padabajka.core.networking

import com.fp.padabajka.core.networking.config.KtorConfigProvider
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class KtorClientProvider internal constructor(
    private val configProviders: List<KtorConfigProvider>,
) {
    private var client: HttpClient? = null

    private val mutex: Mutex = Mutex()

    @Throws(Exception::class)
    suspend fun client(): HttpClient {
        mutex.withLock {
            val resultConfigProviders = configProviders.takeIf { client != null }
                ?.filter { it.needUpdate() }
                ?: configProviders

            val config = resultConfigProviders.resultConfig()

            return if (config != null) {
                val newClient = (client ?: createDefaultClient())

                newClient.config {
                    plusAssign(config)
                }.apply { client = this }
            } else {
                client ?: TODO("HttpClient create exception")
            }
        }
    }

    private suspend fun List<KtorConfigProvider>.resultConfig(): HttpClientConfig<Nothing>? {
        return map { it.config() }
            .reduceOrNull { acc, ktorConfigProvider ->
                acc.plusAssign(ktorConfigProvider)
                acc
            }
    }

    private fun createDefaultClient(): HttpClient {
        return HttpClient(CIO)
    }
}
