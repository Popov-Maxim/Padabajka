package com.padabajka.dating.di

import android.content.Context
import org.koin.dsl.module

fun initKoinAndroid(context: Context) {
    initKoin(module { single { context } })
}
