package com.padabajka.dating.core.permission.di

import com.padabajka.dating.core.permission.AndroidNotificationPermissionController
import com.padabajka.dating.core.permission.NotificationPermissionController
import com.padabajka.dating.core.permission.PermissionHandler
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformPermissionDiModule: Module
    get() = module {
        single<NotificationPermissionController> {
            AndroidNotificationPermissionController(
                permissionHandler = get()
            )
        }
        single {
            PermissionHandler(
                context = get(),
                launcherFactory = { get() }
            )
        }
    }
