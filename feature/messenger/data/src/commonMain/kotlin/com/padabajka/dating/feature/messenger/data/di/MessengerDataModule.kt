package com.padabajka.dating.feature.messenger.data.di

import com.padabajka.dating.core.repository.api.ChatRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.TypingRepository
import com.padabajka.dating.core.repository.api.UserPresenceRepository
import com.padabajka.dating.feature.messenger.data.message.ChatRepositoryImpl
import com.padabajka.dating.feature.messenger.data.message.MessageRepositoryImpl
import com.padabajka.dating.feature.messenger.data.message.UserPresenceRepositoryImpl
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.RoomLocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteChatDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteMessageDataSourceImpl
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.ChatApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.MessageApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.MessageReactionApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.ktor.KtorMessageApi
import com.padabajka.dating.feature.messenger.data.typing.TypingRepositoryImpl
import com.padabajka.dating.feature.messenger.data.typing.source.SocketTypingRemoteDataSource
import com.padabajka.dating.feature.messenger.data.typing.source.TypingRemoteDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val messengerDataModule = module {
    single<ChatRepository> {
        ChatRepositoryImpl(
            remoteChatDataSource = get(),
            localChatDataSource = get()
        )
    }

    factoryOf(::RemoteChatDataSource)
    factoryOf(::LocalChatDataSource)

    single<MessageRepository> {
//        return@single FakeMessengerRepository(
//            scope = get()
//        )
        MessageRepositoryImpl(
            authRepository = get(),
            localMessageDataSource = get(),
            remoteMessageDataSource = get(),
            personRepository = get()
        )
    }
    single<TypingRepository> {
        TypingRepositoryImpl(
            typingRemoteDataSource = get()
        )
    }
    single<UserPresenceRepository> {
        UserPresenceRepositoryImpl()
    }
    factory<TypingRemoteDataSource> {
        SocketTypingRemoteDataSource()
    }
    factory<RemoteMessageDataSource> {
        RemoteMessageDataSourceImpl(
            messageApi = get(),
            chatApi = get(),
            messageReactionApi = get()
        )
    }
    factory<MessageApi> {
        KtorMessageApi(
            ktorClientProvider = get()
        )
    }
    factory<LocalMessageDataSource> {
        RoomLocalMessageDataSource(
            messageDao = get()
        )
    }
    factoryOf(::ChatApi)
    factoryOf(::MessageReactionApi)
}
