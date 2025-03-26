package com.padabajka.dating

import android.app.Application
import com.padabajka.dating.di.initKoinAndroid

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }
}
