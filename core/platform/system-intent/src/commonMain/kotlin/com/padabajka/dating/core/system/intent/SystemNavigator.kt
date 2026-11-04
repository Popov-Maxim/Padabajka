package com.padabajka.dating.core.system.intent

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SystemNavigator(
    private val openMailNavigator: OpenMailNavigator
) {
    suspend fun openMail() {
        withContext(Dispatchers.Main.immediate) {
            openMailNavigator.openMail()
        }
    }

    suspend fun composeMail(address: String, subject: String, body: String): Boolean {
        return withContext(Dispatchers.Main.immediate) {
            openMailNavigator.composeMail(address, subject, body)
        }
    }
}
