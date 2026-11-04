package com.padabajka.dating.core.system.intent

interface OpenMailNavigator {
    suspend fun openMail()

    suspend fun composeMail(address: String, subject: String, body: String): Boolean
}
