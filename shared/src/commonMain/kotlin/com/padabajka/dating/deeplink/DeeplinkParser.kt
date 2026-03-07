package com.padabajka.dating.deeplink

import com.padabajka.dating.core.repository.api.model.messenger.ChatId

class DeeplinkParser {

    fun parse(raw: String): Deeplink? {
        return if (raw.startsWith("https://padabajka-96c95.firebaseapp.com/__/auth/links")) {
            Deeplink.AuthLink(raw)
        } else if (raw.startsWith("$SCHEME://")) {
            parseApplicationDeeplink(raw)
        } else {
            null
        }
    }

    private fun parseApplicationDeeplink(raw: String): Deeplink? {
        val path = raw.removePrefix("$SCHEME://")

        val parts = path.split("/")

        return when (parts.firstOrNull()) {
            "chat" -> {
                val chatId = parts.getOrNull(1)?.let(::ChatId) ?: return null
                AppDeeplink.OpenChat(chatId)
            }

            "likes" -> AppDeeplink.OpenLikes

            else -> null
        }
    }

    private companion object {
        private const val SCHEME = "com.padabajka.dating"
    }
}
