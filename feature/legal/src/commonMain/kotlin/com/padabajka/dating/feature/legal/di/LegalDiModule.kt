package com.padabajka.dating.feature.legal.di

import com.padabajka.dating.core.repository.api.metadata.LegalRepository
import com.padabajka.dating.feature.legal.data.LegalRepositoryImpl
import com.padabajka.dating.feature.legal.data.network.LegalApi
import com.padabajka.dating.feature.legal.domain.LegalVersionsProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val dataModule = module {
    singleOf(::LegalRepositoryImpl) {
        bind<LegalRepository>()
    }
    factoryOf(::LegalApi)
}
private val domainModule = module {
    factoryOf(::LegalVersionsProvider)
}

val legalDiModules = arrayOf(domainModule, dataModule)
