package com.padabajka.dating.core.system.intent.di

import com.padabajka.dating.core.system.intent.IosOpenMailNavigator
import com.padabajka.dating.core.system.intent.OpenMailNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformSystemIntentDiModule: Module
    get() = module {
        factory<OpenMailNavigator> {
            IosOpenMailNavigator()
        }
    }
