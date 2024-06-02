package com.fp.padabajka.di

import com.fp.padabajka.component.room.di.roomModule
import com.fp.padabajka.feature.ads.di.adModules
import com.fp.padabajka.feature.ads.yandex.di.yandexAdModule
import com.fp.padabajka.feature.auth.di.authModules
import com.fp.padabajka.feature.messenger.di.messengerModules
import com.fp.padabajka.feature.profile.di.profileModules
import com.fp.padabajka.feature.swiper.di.swiperModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(platformModule: Module) {
    val applicationScopeModule = module {
        single<CoroutineScope> {
            CoroutineScope(SupervisorJob() + Dispatchers.Default)
        }
    } // TODO(koin): application scope

    startKoin {
        modules(
            *authModules,
            *swiperModules,
            *adModules,
            yandexAdModule,
            *profileModules,
            *messengerModules,
            roomModule,
            platformModule,
            applicationScopeModule
        )
    }
}
