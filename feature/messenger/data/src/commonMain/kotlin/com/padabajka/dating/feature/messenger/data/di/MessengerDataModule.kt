package com.padabajka.dating.feature.messenger.data.di

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.TypingRepository
import com.padabajka.dating.feature.messenger.data.message.MessageRepositoryImpl
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.RoomLocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.KtorMessageApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.MessageApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.remote.RemoteMessageDataSourceImpl
import com.padabajka.dating.feature.messenger.data.typing.TypingRepositoryImpl
import com.padabajka.dating.feature.messenger.data.typing.source.SocketTypingRemoteDataSource
import com.padabajka.dating.feature.messenger.data.typing.source.TypingRemoteDataSource
import org.koin.dsl.module

val messengerDataModule = module {
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
    factory<TypingRemoteDataSource> {
        SocketTypingRemoteDataSource()
    }
    factory<RemoteMessageDataSource> {
        RemoteMessageDataSourceImpl(
            messageApi = get()
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
}
