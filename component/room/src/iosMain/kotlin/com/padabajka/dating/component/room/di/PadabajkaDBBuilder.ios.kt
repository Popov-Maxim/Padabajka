package com.padabajka.dating.component.room.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.padabajka.dating.component.room.DB_NAME
import com.padabajka.dating.component.room.PadabajkaDB
import com.padabajka.dating.component.room.PadabajkaDBConstructor
import org.koin.core.scope.Scope
import platform.Foundation.NSHomeDirectory

actual val Scope.dbBuilder: RoomDatabase.Builder<PadabajkaDB>
    get() {
        val path = NSHomeDirectory() + "/" + DB_NAME
        return Room.databaseBuilder<PadabajkaDB>(
            name = path,
            factory = { PadabajkaDBConstructor.initialize() }
        )
    }
