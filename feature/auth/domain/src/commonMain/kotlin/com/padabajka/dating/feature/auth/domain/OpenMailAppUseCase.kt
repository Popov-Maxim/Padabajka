package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.system.intent.SystemNavigator

class OpenMailAppUseCase(
    private val systemNavigator: SystemNavigator
) {
    suspend operator fun invoke() {
        systemNavigator.openMail()
    }
}
