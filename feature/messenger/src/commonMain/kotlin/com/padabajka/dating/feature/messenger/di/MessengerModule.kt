package com.padabajka.dating.feature.messenger.di

import com.padabajka.dating.feature.messenger.data.di.messengerDataModule
import com.padabajka.dating.feature.messenger.domain.chat.ChatMessagesUseCase
import com.padabajka.dating.feature.messenger.domain.chat.ReactToMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.ReadMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.SendMessageUseCase
import com.padabajka.dating.feature.messenger.domain.chat.StartTypingUseCase
import com.padabajka.dating.feature.messenger.domain.chat.StopTypingUseCase
import com.padabajka.dating.feature.messenger.presentation.chat.ChatComponent
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val domainModule = module {
    factoryOf(::ChatMessagesUseCase)
    factoryOf(::SendMessageUseCase)
    factoryOf(::ReadMessageUseCase)
    factoryOf(::ReactToMessageUseCase)
    factoryOf(::StartTypingUseCase)
    factoryOf(::StopTypingUseCase)
}

private val presentationModule = module {
    factory { parameters ->
        ChatComponent(
            context = parameters.get(),
            chatId = parameters.get(),
            chatMessagesUseCaseFactory = { get() },
            sendMessageUseCaseFactory = { get() },
            readMessageUseCaseFactory = { get() },
            reactToMessageUseCaseFactory = { get() },
            startTypingUseCaseFactory = { get() },
            stopTypingUseCaseFactory = { get() }
        )
    }
}

val messengerModules = arrayOf(messengerDataModule, domainModule, presentationModule)
