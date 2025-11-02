package com.padabajka.dating.component.room.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.padabajka.dating.component.room.DB_NAME
import com.padabajka.dating.component.room.PadabajkaDB
import com.padabajka.dating.component.room.PadabajkaDBConstructor
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val Scope.dbBuilder: RoomDatabase.Builder<PadabajkaDB>
    get() {
        val path = documentDirectory() + "/" + DB_NAME
        return Room.databaseBuilder<PadabajkaDB>(
            name = path,
            factory = { PadabajkaDBConstructor.initialize() }
        )
    }

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
