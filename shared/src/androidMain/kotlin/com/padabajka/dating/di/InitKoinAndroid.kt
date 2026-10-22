package com.padabajka.dating.di

import android.app.Application
import android.content.Context
import com.padabajka.dating.core.permission.PermissionRequestHandler
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

fun initKoinAndroid(application: Application) {
    initKoin(
        module {
            single<Context> { application }
            single<Application> { application }
        }
    )
}

fun addPermissionRequester(
    permissionRequestHandler: PermissionRequestHandler
) {
    loadKoinModules(module { factory { permissionRequestHandler } })
}
