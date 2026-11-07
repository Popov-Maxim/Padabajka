package com.padabajka.dating.core.notification.di

import com.padabajka.dating.core.notification.AndroidNotificationService
import com.padabajka.dating.core.notification.PlatformNotificationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val notificationPlatformModule: Module
    get() = module {
        single<PlatformNotificationService> {
            AndroidNotificationService(
                context = get()
            )
        }
    }
