package com.fp.padabajka.di

import org.koin.dsl.module

// TODO: Call me from IOS
fun initKoinIos() {
    initKoin(module { }) // Todo Add config dependency if needed
}
