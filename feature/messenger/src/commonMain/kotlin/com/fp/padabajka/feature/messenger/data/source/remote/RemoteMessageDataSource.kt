package com.fp.padabajka.feature.messenger.data.source.remote

import com.fp.padabajka.feature.messenger.data.model.MessageDto

interface RemoteMessageDataSource {
    suspend fun sendMessage(matchId: String, content: String): MessageDto
    suspend fun sendReaction(matchId: String, messageId: Long, reaction: String)
    suspend fun readMessages(matchId: String, messageId: Long)
}
