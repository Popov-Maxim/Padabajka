package com.padabajka.dating.feature.reaction.screen.di

import com.padabajka.dating.feature.reaction.screen.domain.ReactionsToMeUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataModule = module {
}

private val domainModule = module {
    factoryOf(::ReactionsToMeUseCase)
}

private val presentationModule = module {
}

val reactionScreenDiModules = arrayOf(dataModule, domainModule, presentationModule)
