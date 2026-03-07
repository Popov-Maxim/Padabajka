package com.padabajka.dating.deeplink

import com.padabajka.dating.core.repository.api.model.messenger.ChatId

sealed interface Deeplink {

    data class AuthLink(val link: String) : Deeplink
}

sealed interface AppDeeplink : Deeplink {
    data class OpenChat(val chatId: ChatId) : AppDeeplink
    data object OpenLikes : AppDeeplink
}
