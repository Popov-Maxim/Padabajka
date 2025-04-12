package com.padabajka.dating.component.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.padabajka.dating.component.room.messenger.MessageDao
import com.padabajka.dating.component.room.messenger.converters.MessageReactionConverters
import com.padabajka.dating.component.room.messenger.converters.MessageStatusConverters
import com.padabajka.dating.component.room.messenger.entry.MessageEntry

@Database(entities = [MessageEntry::class], version = 1)
@TypeConverters(MessageReactionConverters::class, MessageStatusConverters::class)
abstract class PadabajkaDB : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}

internal const val DB_NAME = "padabajka.db"
