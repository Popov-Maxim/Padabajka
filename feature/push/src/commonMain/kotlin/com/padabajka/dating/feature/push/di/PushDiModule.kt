package com.padabajka.dating.feature.push.di

import com.padabajka.dating.feature.push.data.di.dataPushDiModule
import com.padabajka.dating.feature.push.notification.di.notificationDiModules
import com.padabajka.dating.feature.push.socket.di.socketDiModules

val pushDiModules = notificationDiModules + dataPushDiModule + socketDiModules
