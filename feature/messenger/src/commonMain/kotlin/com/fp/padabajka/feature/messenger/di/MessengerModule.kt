package com.fp.padabajka.feature.messenger.di

import com.fp.padabajka.feature.messenger.data.di.messengerDataModule
import com.fp.padabajka.feature.messenger.domain.ChatMessagesUseCase
import com.fp.padabajka.feature.messenger.domain.ReactToMessageUseCase
import com.fp.padabajka.feature.messenger.domain.ReadMessageUseCase
import com.fp.padabajka.feature.messenger.domain.SendMessageUseCase
import com.fp.padabajka.feature.messenger.domain.StartTypingUseCase
import com.fp.padabajka.feature.messenger.domain.StopTypingUseCase
import com.fp.padabajka.feature.messenger.presentation.MessengerComponent
import org.koin.dsl.module

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
    factory {
        StartTypingUseCase(get())
    }
    factory {
        StopTypingUseCase(get())
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
            reactToMessageUseCaseFactory = { get() },
            startTypingUseCaseFactory = { get() },
            stopTypingUseCaseFactory = { get() }
        )
    }
}

val messengerModules = arrayOf(messengerDataModule, domainModule, presentationModule)
