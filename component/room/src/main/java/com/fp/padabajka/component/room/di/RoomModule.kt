package com.fp.padabajka.component.room.di

import androidx.room.Room
import com.fp.padabajka.component.room.PadabajkaDB
import com.fp.padabajka.feature.messenger.data.message.room.MessageDao
import org.koin.dsl.module

val roomModule = module {
    single<PadabajkaDB> {
        Room.databaseBuilder(get(), PadabajkaDB::class.java, "padabajka.db").build()
    }
    factory<MessageDao> {
        val db: PadabajkaDB = get()
        db.messageDao()
    }
}
