package com.padabajka.dating.di

import com.padabajka.dating.component.room.di.roomModule
import com.padabajka.dating.core.domain.di.domainDiModules
import com.padabajka.dating.core.networking.di.networkingModule
import com.padabajka.dating.core.permission.di.permissionDiModules
import com.padabajka.dating.core.platform.data.di.platformDataDiModule
import com.padabajka.dating.core.presentation.di.presentationDiModules
import com.padabajka.dating.core.sync.di.syncDiModule
import com.padabajka.dating.core.system.intent.di.systemIntentDiModule
import com.padabajka.dating.feature.ads.di.adModules
import com.padabajka.dating.feature.auth.di.authModules
import com.padabajka.dating.feature.dictionary.di.dictionaryDiModules
import com.padabajka.dating.feature.image.di.imageModules
import com.padabajka.dating.feature.match.di.matchDiModules
import com.padabajka.dating.feature.messenger.di.messengerModules
import com.padabajka.dating.feature.permission.flow.di.permissionFlowDIModule
import com.padabajka.dating.feature.profile.di.profileModules
import com.padabajka.dating.feature.push.di.pushDiModules
import com.padabajka.dating.feature.reaction.screen.di.reactionScreenDiModules
import com.padabajka.dating.feature.swiper.di.swiperModules
import com.padabajka.dating.settings.di.metadataDiModules
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
    }

    startKoin {
        modules(
            *authModules,
            *swiperModules,
            *adModules,
//            yandexAdModule,
            *profileModules,
            *reactionScreenDiModules,
            networkingModule,
            *domainDiModules,
            *presentationDiModules,
            *platformDataDiModule,
            *systemIntentDiModule,
            *imageModules,
            *settingDiModules,
            *metadataDiModules,
            *dictionaryDiModules,
            *messengerModules,
            *matchDiModules,
            *pushDiModules,
            *permissionDiModules,
            syncDiModule,
            permissionFlowDIModule,
            sharedDiModule,
            roomModule,
            platformModule,
            applicationScopeModule
        )
    }
}
