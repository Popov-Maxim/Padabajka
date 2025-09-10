package com.padabajka.dating.di

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
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

fun addActivity(activity: Activity) {
    loadKoinModules(module { single { activity } })
}

fun addPermissionRequester(
    permissionRequester: ActivityResultLauncher<String>
) {
    loadKoinModules(module { factory { permissionRequester } })
}
