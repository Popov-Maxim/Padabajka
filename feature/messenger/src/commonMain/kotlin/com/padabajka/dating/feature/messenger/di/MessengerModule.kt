package com.padabajka.dating.feature.messenger.di

import com.padabajka.dating.feature.messenger.data.di.messengerDataModule
import com.padabajka.dating.feature.messenger.domain.ChatMessagesUseCase
import com.padabajka.dating.feature.messenger.domain.ReactToMessageUseCase
import com.padabajka.dating.feature.messenger.domain.ReadMessageUseCase
import com.padabajka.dating.feature.messenger.domain.SendMessageUseCase
import com.padabajka.dating.feature.messenger.domain.StartTypingUseCase
import com.padabajka.dating.feature.messenger.domain.StopTypingUseCase
import com.padabajka.dating.feature.messenger.presentation.MessengerComponent
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
        MessengerComponent(
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
