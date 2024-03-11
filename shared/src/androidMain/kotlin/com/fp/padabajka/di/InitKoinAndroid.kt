package com.fp.padabajka.di

import android.content.Context
import org.koin.dsl.module

fun initKoinAndroid(context: Context) {
    initKoin(module { single { context } })
}
