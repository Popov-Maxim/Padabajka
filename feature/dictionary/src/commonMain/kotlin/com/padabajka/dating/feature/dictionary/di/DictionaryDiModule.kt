package com.padabajka.dating.feature.dictionary.di

import com.padabajka.dating.core.repository.api.DictionaryRepository
import com.padabajka.dating.feature.dictionary.data.DictionaryRepositoryImpl
import com.padabajka.dating.feature.dictionary.data.source.StaticWordDataSource
import com.padabajka.dating.feature.dictionary.data.source.StaticWordDataSourceImpl
import com.padabajka.dating.feature.dictionary.data.source.static.StaticDictionaryProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val dataModule = module {
    single<DictionaryRepository> {
        DictionaryRepositoryImpl(
            staticWordDataSource = get(),
            assetRepository = get()
        )
    }

    factory<StaticWordDataSource> {
        StaticWordDataSourceImpl(
            staticDictionaryProvider = get()
        )
    }

    factoryOf(::StaticDictionaryProvider)
}

val dictionaryDiModules = arrayOf(dataModule)
