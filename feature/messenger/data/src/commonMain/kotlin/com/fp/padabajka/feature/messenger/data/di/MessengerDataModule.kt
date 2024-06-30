package com.fp.padabajka.feature.messenger.data.di

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.TypingRepository
import com.fp.padabajka.feature.messenger.data.message.MessageRepositoryImpl
import com.fp.padabajka.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.fp.padabajka.feature.messenger.data.message.source.local.RoomLocalMessageDataSource
import com.fp.padabajka.feature.messenger.data.message.source.remote.RemoteMessageDataSource
import com.fp.padabajka.feature.messenger.data.message.source.remote.RemoteMessageDataSourceImpl
import com.fp.padabajka.feature.messenger.data.typing.TypingRepositoryImpl
import com.fp.padabajka.feature.messenger.data.typing.source.SocketTypingRemoteDataSource
import com.fp.padabajka.feature.messenger.data.typing.source.TypingRemoteDataSource
import org.koin.dsl.module

val messengerDataModule = module {
    single<MessageRepository> {
        MessageRepositoryImpl(get(), get(), get(), get())
    }
    single<TypingRepository> {
        TypingRepositoryImpl(get())
    }
    factory<TypingRemoteDataSource> {
        SocketTypingRemoteDataSource()
    }
    factory<RemoteMessageDataSource> {
        RemoteMessageDataSourceImpl()
    }
    factory<LocalMessageDataSource> {
        RoomLocalMessageDataSource(get())
    }
}
