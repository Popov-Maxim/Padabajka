package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.deeplink.Deeplink

interface DeeplinkHandler {
    suspend fun handle(deeplink: Deeplink)
}
