package com.padabajka.dating.feature.push.notification.di

import com.padabajka.dating.feature.push.notification.NotificationService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platform: Module

private val notification = module {
    singleOf(::NotificationService)
}

val notificationDiModules: Array<Module>
    get() = arrayOf(platform, notification)
