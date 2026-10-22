package com.padabajka.dating.core.system.intent

import android.content.Context
import android.content.Intent

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
}
