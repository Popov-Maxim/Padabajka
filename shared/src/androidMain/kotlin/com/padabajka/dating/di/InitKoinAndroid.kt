package com.padabajka.dating.di

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

fun initKoinAndroid(context: Context) {
    initKoin(module { single { context } })
}

fun addActivity(activity: Activity) {
    loadKoinModules(module { single { activity } })
}

fun addPermissionRequester(
    permissionRequester: ActivityResultLauncher<String>
) {
    loadKoinModules(module { factory { permissionRequester } })
}
