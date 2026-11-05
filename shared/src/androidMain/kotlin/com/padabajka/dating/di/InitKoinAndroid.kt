package com.padabajka.dating.di

import android.app.Application
import android.content.Context
import com.padabajka.dating.core.permission.PermissionRequestHandler
import com.padabajka.dating.core.platform.data.res.ResProvider
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

fun initKoinAndroid(application: Application, resProvider: ResProvider) {
    initKoin(
        module {
            single<Context> { application }
            single<Application> { application }
            single<ResProvider> { resProvider }
        }
    )
}

fun addPermissionRequester(
    permissionRequestHandler: PermissionRequestHandler
) {
    loadKoinModules(module { factory { permissionRequestHandler } })
}
