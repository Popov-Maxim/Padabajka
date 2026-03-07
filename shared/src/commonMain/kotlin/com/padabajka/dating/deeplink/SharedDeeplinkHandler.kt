package com.padabajka.dating.deeplink

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.navigation.RootComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SharedDeeplinkHandler : KoinComponent {

    private val authRepository: AuthRepository by inject()

    private val scope: CoroutineScope by inject()

    private val deeplinkParser: DeeplinkParser by inject()

    fun handle(uri: String, rootComponent: RootComponent) {
        val deeplink = deeplinkParser.parse(uri) ?: return
        scope.launch {
            when (deeplink) {
                is Deeplink.AuthLink -> authRepository.signInWithEmailLink(deeplink.link)
                is AppDeeplink -> rootComponent.onDeeplink(deeplink)
            }
        }
    }
}
