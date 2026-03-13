package com.padabajka.dating.deeplink

import com.padabajka.dating.core.repository.api.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SharedDeeplinkHandler : KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val scope: CoroutineScope by inject()
    private val deeplinkParser: DeeplinkParser by inject()

    private val _appDeeplinks = MutableSharedFlow<AppDeeplink>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val appDeeplinks: SharedFlow<AppDeeplink> = _appDeeplinks.asSharedFlow()

    fun handle(uri: String) {
        val deeplink = deeplinkParser.parse(uri) ?: return
        scope.launch {
            when (deeplink) {
                is Deeplink.AuthLink -> authRepository.signInWithEmailLink(deeplink.link)
                is AppDeeplink -> _appDeeplinks.emit(deeplink)
            }
        }
    }
}
