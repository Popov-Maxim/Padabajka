package com.fp.padabajka.component.room.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.fp.padabajka.component.room.DB_NAME
import com.fp.padabajka.component.room.PadabajkaDB
import org.koin.core.scope.Scope
import platform.Foundation.NSHomeDirectory

actual val Scope.dbBuilder: RoomDatabase.Builder<PadabajkaDB>
    get() {
        val path = NSHomeDirectory() + "/" + DB_NAME
        return Room.databaseBuilder<PadabajkaDB>(
            name = path,
            factory = { PadabajkaDB::class.instantiateImpl() }
        )
    }
