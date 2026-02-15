package com.padabajka.dating.core.platform.data.di

import org.koin.core.module.Module

expect val platformDiModule: Module

val platformDataDiModule = arrayOf(platformDiModule)
