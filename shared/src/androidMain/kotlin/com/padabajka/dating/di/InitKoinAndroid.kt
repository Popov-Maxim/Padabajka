package com.padabajka.dating.di

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

fun initKoinAndroid(context: Context) {
    initKoin(module { single { context } })
}

fun addPermissionRequester(
    permissionRequester: ActivityResultLauncher<String>
) {
    loadKoinModules(module { factory { permissionRequester } })
}
