package com.padabajka.dating.component.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.padabajka.dating.component.room.asset.AssetsDao
import com.padabajka.dating.component.room.asset.city.CityDao
import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.component.room.asset.entry.AssetsTranslationEntry
import com.padabajka.dating.component.room.chat.ChatDao
import com.padabajka.dating.component.room.chat.entry.ChatEntry
import com.padabajka.dating.component.room.matches.MatchesDao
import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.component.room.messenger.MessageDao
import com.padabajka.dating.component.room.messenger.MessageReadEventDao
import com.padabajka.dating.component.room.messenger.converters.MessageReactionEntityConverters
import com.padabajka.dating.component.room.messenger.converters.MessageStatusConverters
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import com.padabajka.dating.component.room.person.PersonDao
import com.padabajka.dating.component.room.person.converters.ListStringConverters
import com.padabajka.dating.component.room.person.converters.LocalDateConverters
import com.padabajka.dating.component.room.person.converters.LookingForDataConverter
import com.padabajka.dating.component.room.person.entry.PersonEntry

@Database(
    entities = [
        MessageEntry::class,
        ChatEntry::class,
        MessageReadEventEntry::class,
        PersonEntry::class,
        MatchEntry::class,
        CityEntry::class,
        AssetsTranslationEntry::class
    ],
    version = 1
)
@TypeConverters(
    MessageReactionEntityConverters::class,
    MessageStatusConverters::class,
    LocalDateConverters::class,
    ListStringConverters::class,
    LookingForDataConverter::class
)
@ConstructedBy(PadabajkaDBConstructor::class)
abstract class PadabajkaDB : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
    abstract fun messageReadEventDao(): MessageReadEventDao
    abstract fun personDao(): PersonDao
    abstract fun matchesDao(): MatchesDao
    abstract fun cityDao(): CityDao
    abstract fun assetsDao(): AssetsDao
}

internal const val DB_NAME = "padabajka.db"

// TODO(): improve
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PadabajkaDBConstructor : RoomDatabaseConstructor<PadabajkaDB> {
    override fun initialize(): PadabajkaDB
}
