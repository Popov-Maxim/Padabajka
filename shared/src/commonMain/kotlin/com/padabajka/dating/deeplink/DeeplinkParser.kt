package com.padabajka.dating.deeplink

import com.padabajka.dating.core.repository.api.model.deeplink.AppDeeplink
import com.padabajka.dating.core.repository.api.model.deeplink.Deeplink
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.app

class DeeplinkParser {

    fun parse(raw: String): Deeplink? {
        val projectId = Firebase.app.options.projectId
        val emailLinkUrl = "https://$projectId.firebaseapp.com/__/auth/links"
        return if (projectId != null && raw.startsWith(emailLinkUrl)) {
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
