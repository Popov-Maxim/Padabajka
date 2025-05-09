package com.padabajka.dating.feature.push.di

import com.padabajka.dating.feature.push.data.di.dataPushDiModule
import com.padabajka.dating.feature.push.notification.di.notificationDiModules

val pushDiModules = notificationDiModules + dataPushDiModule
