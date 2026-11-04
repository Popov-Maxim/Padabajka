package com.padabajka.dating.core.system.intent

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

internal class AndroidOpenMailNavigator(
    private val context: Context
) : OpenMailNavigator {
    override suspend fun openMail() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_EMAIL)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override suspend fun composeMail(address: String, subject: String, body: String): Boolean {
        val uri = Uri.parse(
            "mailto:${Uri.encode(address, "@")}" +
                "?subject=${Uri.encode(subject)}&body=${Uri.encode(body)}"
        )
        val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(intent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }
}
