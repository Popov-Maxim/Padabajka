package com.padabajka.dating.feature.permission.flow.di

import com.padabajka.dating.feature.permission.flow.presentation.PermissionFlowComponent
import org.koin.dsl.module

val permissionFlowDIModule = module {
    factory { parameters ->
        PermissionFlowComponent(
            context = parameters.get(),
            finish = parameters.get(),
            geoPermissionController = get(),
            notificationPermissionController = get(),
        )
    }
}
