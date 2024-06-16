package com.fp.padabajka.component.room.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.fp.padabajka.component.room.PadabajkaDB
import com.fp.padabajka.component.room.messenger.MessageDao
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
}
