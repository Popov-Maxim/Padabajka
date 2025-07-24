package com.padabajka.dating.component.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.padabajka.dating.component.room.asset.city.CityDao
import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.component.room.asset.city.entry.CityTranslation
import com.padabajka.dating.component.room.matches.MatchesDao
import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.component.room.messenger.MessageDao
import com.padabajka.dating.component.room.messenger.converters.MessageReactionEntityConverters
import com.padabajka.dating.component.room.messenger.converters.MessageStatusConverters
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.person.PersonDao
import com.padabajka.dating.component.room.person.converters.ListStringConverters
import com.padabajka.dating.component.room.person.converters.LocalDateConverters
import com.padabajka.dating.component.room.person.converters.LookingForDataConverter
import com.padabajka.dating.component.room.person.entry.PersonEntry

@Database(
    entities = [MessageEntry::class, PersonEntry::class, MatchEntry::class, CityEntry::class, CityTranslation::class],
    version = 1
)
@TypeConverters(
    MessageReactionEntityConverters::class,
    MessageStatusConverters::class,
    LocalDateConverters::class,
    ListStringConverters::class,
    LookingForDataConverter::class
)
abstract class PadabajkaDB : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun personDao(): PersonDao
    abstract fun matchesDao(): MatchesDao
    abstract fun cityDao(): CityDao
}

internal const val DB_NAME = "padabajka.db"
