package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.matches.MatchesDao
import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatDataSource
import kotlinx.coroutines.flow.Flow

class RoomLocalMatchDataSource(
    private val matchesDao: MatchesDao,
    private val chatDataSource: LocalChatDataSource
) : LocalMatchDataSource {
    override fun matches(): Flow<List<MatchEntry>> {
        return matchesDao.matches()
    }

    override fun findMatch(chatId: ChatId): Flow<MatchEntry?> {
        return matchesDao.findMatch(chatId.raw)
    }

    override suspend fun saveMatch(match: MatchEntry) {
        chatDataSource.getOrCreateChat(ChatId(match.chatId))
        matchesDao.insertOrUpdate(match)
    }

    override suspend fun replaceMatches(matches: List<MatchEntry>) {
        val chatIds = matches.map { it.chatId.run(::ChatId) }
        chatIds.forEach { chatDataSource.getOrCreateChat(it) }
        matchesDao.replaceMatchesForUser(matches)
        chatDataSource.deleteAllExcept(chatIds)
    }

    override suspend fun delete(matchId: Match.Id) {
        val match = matchesDao.getMatch(matchId.raw) ?: return
        matchesDao.delete(match.id)
        chatDataSource.deleteChat(ChatId(match.chatId))
    }

    override suspend fun update(
        matchId: Match.Id,
        updated: (MatchEntry) -> MatchEntry
    ) {
        val match = matchesDao.getMatch(matchId.raw) ?: return
        val updatedMatch = updated(match)

        val oldChatId = ChatId(match.chatId)
        if (oldChatId.raw != updatedMatch.chatId) {
            chatDataSource.deleteChat(oldChatId)
        }

        matchesDao.insertOrUpdate(updatedMatch)
    }
}
