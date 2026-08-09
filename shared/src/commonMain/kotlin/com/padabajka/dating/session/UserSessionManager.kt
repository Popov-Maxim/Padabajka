package com.padabajka.dating.session

import com.padabajka.dating.component.room.session.UserSessionDao
import com.padabajka.dating.core.repository.api.CandidateRepository
import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.DraftProfileRepository
import com.padabajka.dating.core.repository.api.GeoRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.UserPresenceRepository
import com.padabajka.dating.core.repository.api.metadata.LegalRepository
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.feature.push.notification.platform.PlatformNotificationService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserSessionManager(
    private val localSessionDataSource: LocalUserSessionDataSource,
    private val userSessionDao: UserSessionDao,
    private val profileRepository: ProfileRepository,
    private val draftProfileRepository: DraftProfileRepository,
    private val reactionRepository: ReactionRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val candidateRepository: CandidateRepository,
    private val cardRepository: CardRepository,
    private val searchPreferencesRepository: SearchPreferencesRepository,
    private val geoRepository: GeoRepository,
    private val metadataRepository: MetadataRepository,
    private val legalRepository: LegalRepository,
    private val messageRepository: MessageRepository,
    private val userPresenceRepository: UserPresenceRepository,
    private val notificationService: PlatformNotificationService,
) {
    private val mutex = Mutex()

    suspend fun prepare(userId: UserId) = mutex.withLock {
        if (localSessionDataSource.ownerId() == userId.raw) return@withLock

        clearUserData()
        localSessionDataSource.setOwnerId(userId.raw)
    }

    suspend fun clear() = mutex.withLock {
        clearUserData()
        localSessionDataSource.setOwnerId(null)
    }

    suspend fun runIfActive(userId: UserId, block: suspend () -> Unit) = mutex.withLock {
        if (localSessionDataSource.ownerId() != userId.raw) return@withLock

        block()
    }

    private suspend fun clearUserData() {
        // Stop in-memory producers before clearing their persistent targets.
        messageRepository.clearLocalData()
        candidateRepository.clearLocalData()
        cardRepository.clearLocalData()

        userSessionDao.clearUserData()
        profileRepository.clearLocalData()
        draftProfileRepository.clearLocalData()
        reactionRepository.clearLocalData()
        subscriptionRepository.clearLocalData()
        searchPreferencesRepository.clearLocalData()
        geoRepository.clearLocalData()
        metadataRepository.clearLocalData()
        legalRepository.clearLocalData()
        userPresenceRepository.clearLocalData()
        notificationService.clearAllNotifications()
    }
}
