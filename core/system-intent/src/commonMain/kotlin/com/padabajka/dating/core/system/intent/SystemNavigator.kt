package com.padabajka.dating.core.system.intent

class SystemNavigator(
    private val openMailNavigator: OpenMailNavigator
) {
    suspend fun openMail() {
        openMailNavigator.openMail()
    }
}
