package com.padabajka.dating.deeplink

import com.padabajka.dating.core.repository.api.DeeplinkHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SharedDeeplinkHandler : KoinComponent {

    private val scope: CoroutineScope by inject()
    private val deeplinkParser: DeeplinkParser by inject()
    private val deeplinkHandler: DeeplinkHandler by inject()

    fun handle(uri: String) {
        val deeplink = deeplinkParser.parse(uri) ?: return
        scope.launch {
            deeplinkHandler.handle(deeplink)
        }
    }
}
