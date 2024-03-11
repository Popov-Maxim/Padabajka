package com.fp.padabajka

import android.app.Application
import com.fp.padabajka.di.initKoinAndroid

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }
}