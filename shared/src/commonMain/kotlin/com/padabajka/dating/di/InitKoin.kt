package com.padabajka.dating.di

import com.padabajka.dating.core.domain.di.domainDiModules
import com.padabajka.dating.core.networking.di.networkingModule
import com.padabajka.dating.core.presentation.di.presentationDiModules
import com.padabajka.dating.feature.ads.di.adModules
import com.padabajka.dating.feature.auth.di.authModules
import com.padabajka.dating.feature.image.di.imageModules
import com.padabajka.dating.feature.profile.di.profileModules
import com.padabajka.dating.feature.swiper.di.swiperModules
import com.padabajka.dating.settings.di.settingDiModules
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
//            yandexAdModule,
            *profileModules,
            networkingModule,
            *domainDiModules,
            *presentationDiModules,
            *imageModules,
            *settingDiModules,
            platformModule,
            applicationScopeModule
        )
    }
}
