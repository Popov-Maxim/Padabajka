package com.fp.padabajka.component.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fp.padabajka.feature.messenger.data.message.room.MessageDao
import com.fp.padabajka.feature.messenger.data.message.room.MessageEntry
import com.fp.padabajka.feature.messenger.data.message.room.converter.MessageReactionConverters
import com.fp.padabajka.feature.messenger.data.message.room.converter.MessageStatusConverters

@Database(entities = [MessageEntry::class], version = 1)
@TypeConverters(MessageReactionConverters::class, MessageStatusConverters::class)
abstract class PadabajkaDB : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
