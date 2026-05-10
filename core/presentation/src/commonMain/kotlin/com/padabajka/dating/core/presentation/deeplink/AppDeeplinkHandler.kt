package com.padabajka.dating.core.presentation.deeplink

import com.padabajka.dating.core.repository.api.model.deeplink.AppDeeplink
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppDeeplinkHandler {
    private val _appDeeplinks = MutableSharedFlow<AppDeeplink>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val appDeeplinks: SharedFlow<AppDeeplink> = _appDeeplinks.asSharedFlow()

    suspend fun handle(deeplink: AppDeeplink) {
        _appDeeplinks.emit(deeplink)
    }
}
