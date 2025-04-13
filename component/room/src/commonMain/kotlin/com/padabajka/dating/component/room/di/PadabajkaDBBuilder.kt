package com.padabajka.dating.component.room.di

import androidx.room.RoomDatabase
import com.padabajka.dating.component.room.PadabajkaDB
import org.koin.core.scope.Scope

expect val Scope.dbBuilder: RoomDatabase.Builder<PadabajkaDB>
