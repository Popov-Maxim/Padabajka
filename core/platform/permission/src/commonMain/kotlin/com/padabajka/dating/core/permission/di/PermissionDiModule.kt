package com.padabajka.dating.core.permission.di

import org.koin.core.module.Module

expect val platformPermissionDiModule: Module

val permissionDiModules
    get() = arrayOf(platformPermissionDiModule)
