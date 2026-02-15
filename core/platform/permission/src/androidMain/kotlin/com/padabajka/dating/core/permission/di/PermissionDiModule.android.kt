package com.padabajka.dating.core.permission.di

import com.padabajka.dating.core.permission.GeoPermissionController
import com.padabajka.dating.core.permission.NotificationPermissionController
import com.padabajka.dating.core.permission.PermissionHandler
import com.padabajka.dating.core.permission.geo.AndroidGeoPermissionController
import com.padabajka.dating.core.permission.notification.AndroidNotificationPermissionController
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformPermissionDiModule: Module
    get() = module {
        single {
            PermissionHandler(
                context = get(),
                permissionRequestHandlerFactory = { get() }
            )
        }
        single<NotificationPermissionController> {
            AndroidNotificationPermissionController(
                permissionHandler = get()
            )
        }
        single<GeoPermissionController> {
            AndroidGeoPermissionController(
                permissionHandler = get()
            )
        }
    }
