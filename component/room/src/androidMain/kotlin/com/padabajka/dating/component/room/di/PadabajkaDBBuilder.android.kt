package com.padabajka.dating.component.room.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.padabajka.dating.component.room.DB_NAME
import com.padabajka.dating.component.room.PadabajkaDB
import org.koin.core.scope.Scope

actual val Scope.dbBuilder: RoomDatabase.Builder<PadabajkaDB>
    get() {
        val app: Context = get()
        val dbFile = app.getDatabasePath(DB_NAME)
        return Room.databaseBuilder<PadabajkaDB>(
            context = get(),
            name = dbFile.absolutePath
        )
    }
