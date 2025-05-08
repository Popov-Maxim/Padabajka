package com.padabajka.dating.core.permission.di

import com.padabajka.dating.core.permission.IosNotificationPermissionController
import com.padabajka.dating.core.permission.NotificationPermissionController
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformPermissionDiModule: Module
    get() = module {
        single<NotificationPermissionController> { IosNotificationPermissionController() }
    }
