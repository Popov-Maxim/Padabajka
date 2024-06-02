package com.fp.padabajka.feature.messenger.di

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.feature.messenger.data.FakeMessengerRepository
import com.fp.padabajka.feature.messenger.domain.ChatMessagesUseCase
import com.fp.padabajka.feature.messenger.domain.ReactToMessageUseCase
import com.fp.padabajka.feature.messenger.domain.ReadMessageUseCase
import com.fp.padabajka.feature.messenger.domain.SendMessageUseCase
import com.fp.padabajka.feature.messenger.presentation.MessengerComponent
import org.koin.dsl.module

private val dataModule = module {
    single<MessageRepository> {
        FakeMessengerRepository(get())
    }
}

private val domainModule = module {
    factory {
        ChatMessagesUseCase(get())
    }
    factory {
        SendMessageUseCase(get())
    }
    factory {
        ReadMessageUseCase(get())
    }
    factory {
        ReactToMessageUseCase(get())
    }
}

private val presentationModule = module {
    factory { parameters ->
        MessengerComponent(
            context = parameters.get(),
            chatId = parameters.get(),
            chatMessagesUseCaseFactory = { get() },
            sendMessageUseCaseFactory = { get() },
            readMessageUseCaseFactory = { get() },
            reactToMessageUseCaseFactory = { get() }
        )
    }
}

val messengerModules = arrayOf(dataModule, domainModule, presentationModule)
