package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.toReadEventEntry
import com.padabajka.dating.core.data.MutableAtomic
import com.padabajka.dating.core.data.atomic
import com.padabajka.dating.core.data.mutableAtomic
import com.padabajka.dating.core.data.uuid
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.model.auth.NoAuthorisedUserException
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.data.message.model.toRequest
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatReadStateDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteChatReadStateDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class ReadMessageManager(
    private val localMessageDataSource: LocalMessageDataSource,
    private val localChatReadStateDataSource: LocalChatReadStateDataSource,
    private val remoteChatReadStateDataSource: RemoteChatReadStateDataSource,
    private val authRepository: AuthRepository,
    private val scope: CoroutineScope
) {

    private val myPersonId: UserId
        get() = authRepository.currentAuthState.userIdOrNull() ?: throw NoAuthorisedUserException

    private val readFlow = MutableSharedFlow<MessageId>(extraBufferCapacity = 100)
    private val processorJob: MutableAtomic<Job?> = mutableAtomic(null)

    suspend fun markRead(messageId: MessageId) {
        processorJob.update {
            return@update this?.takeIf { it.isActive } ?: scope.launch {
                processFlow()
            }
        }

        readFlow.emit(messageId)
    }

    @OptIn(FlowPreview::class)
    @Suppress("TooGenericExceptionCaught")
    private suspend fun processFlow() {
        try {
            readFlow
                .bufferedBatch(timeoutMillis = 500)
                .collect { lastMessageIds ->
                    val messages = lastMessageIds.map {
                        localMessageDataSource.message(it.raw)
                    }

                    val newLastReadMessage = messages.filter { myPersonId.raw != it.authorId }
                        .maxBy(MessageEntry::creationTime)

                    handleReadEvent(newLastReadMessage)
                }
        } catch (e: Throwable) {
            println("ReadMessageManager crashed: ${e.message}, restarting...")
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private suspend fun handleReadEvent(message: MessageEntry) {
        val messageReadEventEntry = message.toReadEventEntry(
            userId = myPersonId,
            readAt = nowMilliseconds(),
            readSynced = false
        )
        val messageReadEvent =
            localChatReadStateDataSource.addMessageReadEvent(messageReadEventEntry)

        try {
            val (chatId, request) = messageReadEventEntry.toRequest()

            remoteChatReadStateDataSource.sendReadState(chatId, request)

            localChatReadStateDataSource.updateMessageReadEvent(messageReadEvent.id) {
                it.copy(readSynced = true)
            }
        } catch (e: Throwable) {
            // TODO: retry read updating
        }
    }

    private fun nowMilliseconds(): Long = Clock.System.now().toEpochMilliseconds()

    private fun <T> Flow<T>.bufferedBatch(timeoutMillis: Long): Flow<List<T>> = channelFlow {
        val buffer = atomic(mutableListOf<T>())
        val sleepingTimer = SleepingTimer()

        collect { value ->
            println("SleepingTimer: collect $value")
            buffer.invoke { add(value) }

            launch {
                sleepingTimer.delayOnce(timeoutMillis) {
                    buffer.invoke {
                        send(toList())
                        clear()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAtomicApi::class)
class SleepingTimer {

    private val id = mutableAtomic<String?>(null)

    suspend fun delayOnce(timeMs: Long, action: suspend () -> Unit) {
        val newId = uuid()
        id.update { newId }

        delay(timeMs)
        if (id.invoke { this == newId }) {
            action()
        }
    }
}
