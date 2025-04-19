package com.padabajka.dating.component.room.matches.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
class MatchEntry(
    @PrimaryKey val id: String,
    @ColumnInfo("personId") val personId: String,
    @ColumnInfo("chatId") val chatId: String,
    @ColumnInfo("creationTime") val creationTime: Long
)
