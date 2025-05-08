package com.padabajka.dating.feature.push.notification.di

import com.padabajka.dating.feature.push.notification.platform.AndroidNotificationService
import com.padabajka.dating.feature.push.notification.platform.PlatformNotificationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platform: Module
    get() = module {
        single<PlatformNotificationService> {
            AndroidNotificationService(
                context = get()
            )
        }
    }
