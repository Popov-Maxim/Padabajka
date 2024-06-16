package com.fp.padabajka.component.room.di

import androidx.room.RoomDatabase
import com.fp.padabajka.component.room.PadabajkaDB
import org.koin.core.scope.Scope

expect val Scope.dbBuilder: RoomDatabase.Builder<PadabajkaDB>
