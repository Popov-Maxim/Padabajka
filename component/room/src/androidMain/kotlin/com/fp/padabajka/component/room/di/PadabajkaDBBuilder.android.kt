package com.fp.padabajka.component.room.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.fp.padabajka.component.room.DB_NAME
import com.fp.padabajka.component.room.PadabajkaDB
import org.koin.core.scope.Scope

actual val Scope.dbBuilder: RoomDatabase.Builder<PadabajkaDB>
    get() = Room.databaseBuilder<PadabajkaDB>(
        context = get(),
        name = DB_NAME
    )
