package com.fp.padabajka.component.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fp.padabajka.component.room.messenger.MessageDao
import com.fp.padabajka.component.room.messenger.converters.MessageReactionConverters
import com.fp.padabajka.component.room.messenger.converters.MessageStatusConverters
import com.fp.padabajka.component.room.messenger.entry.MessageEntry

@Database(entities = [MessageEntry::class], version = 1)
@TypeConverters(MessageReactionConverters::class, MessageStatusConverters::class)
abstract class PadabajkaDB : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}

internal const val DB_NAME = "padabajka.db"
