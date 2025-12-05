package com.padabajka.dating.component.room.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.padabajka.dating.component.room.PadabajkaDB
import com.padabajka.dating.component.room.asset.city.CityDao
import com.padabajka.dating.component.room.asset.language.LanguageDao
import com.padabajka.dating.component.room.matches.MatchesDao
import com.padabajka.dating.component.room.messenger.MessageDao
import com.padabajka.dating.component.room.person.PersonDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val roomModule = module {
    single {
        dbBuilder.setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    factory<MessageDao> {
        val db: PadabajkaDB = get()
        db.messageDao()
    }

    factory<PersonDao> {
        val db: PadabajkaDB = get()
        db.personDao()
    }

    factory<MatchesDao> {
        val db: PadabajkaDB = get()
        db.matchesDao()
    }

    factory<CityDao> {
        val db: PadabajkaDB = get()
        db.cityDao()
    }

    factory<LanguageDao> {
        val db: PadabajkaDB = get()
        db.languageDao()
    }
}
